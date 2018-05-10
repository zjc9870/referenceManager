$(document).ready(function (){
    init();

    // $("#dropBox, #fileUpload").html5Uploader({
    //     name: "foo",
    //     postUrl: "/manager/uploadFile"
    // });


    $(function(){
        $('#referenceForm').validate({
            rules:{
                name:{
                    required:true,  //必填。如果验证方法不需要参数，则配置为true
                },
                authors:{
                    required:true,
                },
                year:{
                    required:true,
                    number:true,
                    min:0
                },
                conference:{
                    required:true,
                },jh:{
                    required:true,
                    number:true,
                    min:0
                },qh:{
                    required:false,
                    number:true,
                    min:0
                },
                beginPage:{
                    required:true,
                    number:true,
                    min:0
                },
                endPage:{
                    required:true,
                    number:true,
                    min:'#beginPage'
                }
            }
        })
    })

    $('#li1').addClass("am-active");
    $('#tab1').addClass("am-active");
    $('#tab1').addClass("am-in");

    $('#li1').click(function() {
        $('#li2').removeClass("am-active");
        $('#tab2').removeClass("am-active");
        $('#tab2').removeClass("am-in");
        $('#li1').addClass("am-active");
        $('#tab1').addClass("am-active");
        $('#tab1').addClass("am-in");
    });
    $('#li2').click(function() {
        $('#li1').removeClass("am-active");
        $('#tab1').removeClass("am-active");
        $('#tab1').removeClass("am-in");
        $('#li2').addClass("am-active");
        $('#tab2').addClass("am-active");
        $('#tab2').addClass("am-in");
    });
    $('#submit').click(function(){
        var dblpStr = $('#content').val();
        var researchId = $('#researchId').val();
        if(dblpStr==""){
            swal("错误","请输入dblp字符串","error");
        }else{
            $.ajax({
                type:"POST",
                url:"/manager/addReferenceDblp",
                traditional:true,
                dataType:"json",
                data:{researchId:researchId,dblpStr:dblpStr},
                success: function(data){
                    var errorCount = data.errorCount;
                    var errorMsg = data.errorMsg;
                    if(errorMsg=="无法导入"){
                        swal("错误","无法导入","error");
                    }else {
                        if (errorCount >= 1) {
                            swal({
                                title:"导入完成！",
                                text:"有" + errorCount + "条条目无法导入！",
                                type:"success",
                            },function () {
                                window.location.href = "/manager/researchDetail?id=" + researchId;
                            })
                        } else {
                            swal({
                                title:"导入完成！",
                                text:"全部导入完成",
                                type:"success",
                            },function () {
                                window.location.href = "/manager/researchDetail?id=" + researchId;
                            })
                        }
                    }
                }
            })
        }
    });
});



