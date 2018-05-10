$(document).ready(function () {
    init();
    $('#search').click(function(){
        var keywords = $('#keywords').val();
        $('#currentKeywords').val(keywords);
        $('#currentPage').val(0);
        $.ajax({
            type:"POST",
            url:"/search/getSearchResult",
            traditional:true,
            dataType:"json",
            data:{keywords: keywords},
            success: function(data){
                var result = data.resultList;
                var str = "";
                var i=0;
                for(i;i<result.length;i++){
                    var url = result[i].downloadUrl;
                    str+="<div  class=\"am-form-group am-cf\">\n" +
                        "   <div class=\"you\">"+result[i].dblpStr+"</div>\n" +
                            "<a  class=\"am-btn am-btn-default\"  href='"+url+"' ><span class=\"am-icon-download\"></span> 下载链接</a>" +
                            "<div  class=\"am-btn am-btn-default\"   onclick='addToResearch(\""+ result[i].dblpStr +"\")'><span class=\"am-icon-plus\"></span> 加入研究</div>" +
                        "   <hr />" +
                        "</div>";
                }

                $('#resultForm').show();
                $('#resultTitle').show();
                $('#resultPanel').show();
                $('#resultPanel').html(str);
                var nextStr = "";
                if(i>=30){
                    nextStr+="<div class=\"you\" style=\"margin-left: 11%;\">\n" +
                        "<div id=\"nextPage\" class=\"am-btn am-btn-success am-radius\" onclick='nextPage()'>查找更多</div>\n" +
                        "\n" +
                        "</div>";
                    $('#nextPagePanel').show();
                    $('#nextPagePanel').html(nextStr);
                }
            }
        })
    });
})

function nextPage(){
    var keywords = $('#currentKeywords').val();
    var currentPage = $('#currentPage').val();
    $.ajax({
        type: "POST",
        url: "/search/getNextPage",
        traditional: true,
        dataType: "json",
        data: {keywords: keywords,currentPage: currentPage+1},
        success: function (data) {
            $('#nextPagePanel').hide();
            $('#currentPage').val(currentPage+1);
            var result = data.resultList;
            var str = "";
            var i=0;
            for(i;i<result.length;i++){
                var url = result[i].downloadUrl;
                str+="<div  class=\"am-form-group am-cf\">\n" +
                    "   <div class=\"you\">"+result[i].dblpStr+"</div>\n" +
                    "<a  class=\"am-btn am-btn-default\"   href='"+url+"'><span class=\"am-icon-download\"></span> 下载链接</a>" +
                    "<div  class=\"am-btn am-btn-default\"   onclick='addToResearch("+ result[i].dblpStr +")'><span class=\"am-icon-plus\"></span> 加入研究</div>" +
                    "   <hr />" +
                    "</div>";
            }
            $('#resultForm').show();
            $('#resultTitle').show();
            $('#resultPanel').show();
            $('#resultPanel').append(str);

            var nextStr = "";
            if(i>=30){
                nextStr+="<div class=\"you\" style=\"margin-left: 11%;\">\n" +
                    "<div id=\"nextPage\" class=\"am-btn am-btn-success am-radius\" onclick='nextPage()'>查找更多</div>\n" +
                    "</div>";
                $('#nextPagePanel').show();
                $('#nextPagePanel').html(nextStr);
            }
        }
    });
}


function addToResearch(dblpStr){
    $.ajax({
        type:"POST",
        url:"/manager/getAllResearch",
        traditional:true,
        dataType:"json",
        success: function(data){
            var researchList = data.AllResearch;
            var str = "";
            for(var i=0;i<researchList.length;i++){
                str+="<option value='"+researchList[i].id+"' id='"+researchList[i].id+"'>"+researchList[i].name+"</option>"
            }
            swal({
                title: "<small>请选择想要加入到的研究</small>!",
                text: "<form class=\"am-form\">"+
                "<select class=\"am-input-sm\" id=\"value\" name=\"value\" th:value=\"${referenceVo.lx}\">\n" +
                str+
                "</select></form>",
                html: true,
                closeOnConfirm: false,
                closeOnCancel: false,
            },function(){
                var researchId = $('#value').val();
                saveNewReferenceToResearch(researchId,dblpStr);
                swal("成功！", "已经将文献加入到研究！","success");
            })
        }
    })
}

function saveNewReferenceToResearch(researchId,dblpStr) {
    $.ajax({
        type:"POST",
        url:"/manager/saveNewReferenceToResearch",
        traditional:true,
        dataType:"json",
        data:{researchId:researchId,dblpStr:dblpStr},
        success: function(data){
        }
    })
}