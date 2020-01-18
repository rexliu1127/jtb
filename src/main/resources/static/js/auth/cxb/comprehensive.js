$(function () {
    //芝麻信用分数 判断显示不同颜色
    var sesamePointsYuanSu=document.getElementById("sesamePoints");
    if(sesamePointsYuanSu){
        var sesamePoints=document.getElementById("sesamePoints").innerHTML;
        if(sesamePoints<600){
            document.getElementById("sesamePointsDiv").style.backgroundColor="#f2af0e";
            document.getElementById("sesamePointsDiv").style.color="white";
        }
    }else{
        document.getElementById("sesamePointsDiv").style.backgroundColor="#f2af0e";
        document.getElementById("sesamePointsDiv").style.color="white";
    };
    //淘宝芝麻信用分数 判断显示不同颜色
    var taobaoSesamePointsYuanSu=document.getElementById("taobaoSesamePoints");
    if(taobaoSesamePointsYuanSu){
        var taobaoSesamePoints=document.getElementById("taobaoSesamePoints").innerHTML;
        if(taobaoSesamePoints<600){
            document.getElementById("taobaoSesamePointsDiv").style.backgroundColor="#f2af0e";
            document.getElementById("taobaoSesamePointsDiv").style.color="white";
        }
    }else{
        document.getElementById("taobaoSesamePointsDiv").style.backgroundColor="#f2af0e";
        document.getElementById("taobaoSesamePointsDiv").style.color="white";
    };
    //姓名是否一致 判断显示不同颜色
    var nameCheckYuanSu=document.getElementById("nameCheck");
    if(nameCheckYuanSu){
        var nameCheck=document.getElementById("nameCheck").innerHTML;
        if(nameCheck=="不一致"){
            document.getElementById("nameCheckDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("nameCheckDiv").style.color="white";
        }
    }else{
        document.getElementById("nameCheckDiv").style.backgroundColor="#fe4f5b";
        document.getElementById("nameCheckDiv").style.color="white";
    }
    //身份证是否一致 判断显示不同颜色
    var idCardCheckYuanSu=document.getElementById("idCardCheck");
    if(idCardCheckYuanSu){
        var idCardCheck=document.getElementById("idCardCheck").innerHTML;
        if(idCardCheck=="不一致"){
            document.getElementById("idCardCheckDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("idCardCheckDiv").style.color="white";
        }
    }else{
        document.getElementById("idCardCheckDiv").style.backgroundColor="#fe4f5b";
        document.getElementById("idCardCheckDiv").style.color="white";
    }
    //注册时间 判断显示不同颜色
    var regTimeYuanSu=document.getElementById("regTime");
    if(regTimeYuanSu){
        var regTime=document.getElementById("regTime").innerHTML;
        var curTime = new Date();
        var regTimeDate = new Date(Date.parse(regTime));
        curTime.setMonth( curTime.getMonth()-6 );
        if(regTimeDate>=curTime){
            document.getElementById("regTimeDiv").style.backgroundColor="#f2af0e";
            document.getElementById("regTimeDiv").style.color="white";
        }
    }else{
        document.getElementById("regTimeDiv").style.backgroundColor="#f2af0e";
        document.getElementById("regTimeDiv").style.color="white";
    }
    //身份证号命中黑名单次数 判断显示不同颜色
    var cardhitCountYuanSu=document.getElementById("cardhitCount");
    if(cardhitCountYuanSu){
        var cardhitCount=document.getElementById("cardhitCount").innerHTML;
        var newCardhitCount=parseInt(cardhitCount);
        if(newCardhitCount>=1){
            document.getElementById("cardhitCountDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("cardhitCountDiv").style.color="white";
        }
    }
    //紧急联系人通话记录 判断显示不同颜色
    var emergencyCallCountYuanSu=document.getElementById("emergencyCallCount");
    if(emergencyCallCountYuanSu){
        var emergencyCallCount=document.getElementById("emergencyCallCount").innerHTML;
        var newemergencyCallCount=parseInt(emergencyCallCount);
        if(newemergencyCallCount<=15){
            document.getElementById("emergencyCallCountDiv").style.backgroundColor="#f2af0e";
            document.getElementById("emergencyCallCountDiv").style.color="white";
        }
    }else{
        document.getElementById("emergencyCallCountDiv").style.backgroundColor="#f2af0e";
        document.getElementById("emergencyCallCountDiv").style.color="white";
    }
    //机构Tj查询次数 判断显示不同颜色
    var searchedCntYuanSu=document.getElementById("searchedCnt");
    if(searchedCntYuanSu){
        var searchedCnt=document.getElementById("searchedCnt").innerHTML;
        var newsearchedCnt=parseInt(searchedCnt);
        if(newsearchedCnt>150){
            document.getElementById("searchedCntDiv").style.backgroundColor="#f2af0e";
            document.getElementById("searchedCntDiv").style.color="white";
        }
    }else{
        document.getElementById("searchedCntDiv").style.backgroundColor="#f2af0e";
        document.getElementById("searchedCntDiv").style.color="white";
    }

    //号码静默情况 判断显示不同颜色
    var maxIntervalYuanSu=document.getElementById("maxInterval");
    if(maxIntervalYuanSu){
        var maxInterval=document.getElementById("maxInterval").innerHTML;
        var newMaxInterval=parseInt(maxInterval);
        if(newMaxInterval>=5){
            document.getElementById("maxIntervalDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("maxIntervalDiv").style.color="white";
        }
    }

    //本人逾期数 判断显示不同颜色
    var blacklistCntYuanSu=document.getElementById("blacklistCnt");
    if(blacklistCntYuanSu){
        var blacklistCnt=document.getElementById("blacklistCnt").innerHTML;
        var newBlacklistCnt=parseInt(blacklistCnt);
        if(newBlacklistCnt>=1){
            document.getElementById("blacklistCntDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("blacklistCntDiv").style.color="white";
        }
    }

    //夜间通话占比 判断显示不同颜色
    var monthlyAvgSecondsRatioYuanSu=document.getElementById("monthlyAvgSecondsRatio");
    if(monthlyAvgSecondsRatioYuanSu){
        var monthlyAvgSecondsRatio=document.getElementById("monthlyAvgSecondsRatio").innerHTML;
        var newMonthlyAvgSecondsRatio=parseFloat(monthlyAvgSecondsRatio);
        if(newMonthlyAvgSecondsRatio>=10){
            document.getElementById("monthlyAvgSecondsRatioDiv").style.backgroundColor="#f2af0e";
            document.getElementById("monthlyAvgSecondsRatioDiv").style.color="white";
        }
    }

    //用户黑名单比例 判断显示不同颜色
    var contactsBlacklistRatioYuanSu=document.getElementById("contactsBlacklistRatio");
    if(contactsBlacklistRatioYuanSu){
        var contactsBlacklistRatio=document.getElementById("contactsBlacklistRatio").innerHTML;
        var newContactsBlacklistRatio=parseFloat(contactsBlacklistRatio);
        if(newContactsBlacklistRatio>=40){
            document.getElementById("contactsBlacklistRatioDiv").style.backgroundColor="#f2af0e";
            document.getElementById("contactsBlacklistRatioDiv").style.color="white";
        }
    }

    //互通电话数量 判断显示不同颜色
    var bothCallCntYuanSu=document.getElementById("bothCallCnt");
    if(bothCallCntYuanSu){
        var bothCallCnt=document.getElementById("bothCallCnt").innerHTML;
        var newBothCallCnt=parseInt(bothCallCnt);
        if(newBothCallCnt<15){
            document.getElementById("bothCallCntDiv").style.backgroundColor="#f2af0e";
            document.getElementById("bothCallCntDiv").style.color="white";
        }
    }else{
        document.getElementById("bothCallCntDiv").style.backgroundColor="#f2af0e";
        document.getElementById("bothCallCntDiv").style.color="white";
    }

    //互通电话数量 判断显示不同颜色
    var callCount110YuanSu=document.getElementById("callCount110");
    if(callCount110YuanSu){
        var callCount110=document.getElementById("callCount110").innerHTML;
        var newCallCount110=parseInt(callCount110);
        if(newCallCount110>=8){
            document.getElementById("callCount110Div").style.backgroundColor="#f2af0e";
            document.getElementById("callCount110Div").style.color="white";
        }
    }

    //贷款公司通话数 判断显示不同颜色
    var callCount005YuanSu=document.getElementById("callCount005");
    if(callCount005YuanSu){
        var callCount005=document.getElementById("callCount005").innerHTML;
        var newCallCount005=parseInt(callCount005);
        if(newCallCount005>=8){
            document.getElementById("callCount005Div").style.backgroundColor="#f2af0e";
            document.getElementById("callCount005Div").style.color="white";
        }
    }

    //互金公司通话数 判断显示不同颜色
    var callCount008YuanSu=document.getElementById("callCount008");
    if(callCount008YuanSu){
        var callCount008=document.getElementById("callCount008").innerHTML;
        var newCallCount008=parseInt(callCount008);
        if(newCallCount008>=8){
            document.getElementById("callCount008Div").style.backgroundColor="#f2af0e";
            document.getElementById("callCount008Div").style.color="white";
        }
    }

    //通讯录有无 判断显示不同颜色
    var MaillistYuanSu=document.getElementById("Maillist");
    if(MaillistYuanSu){
        var Maillist=document.getElementById("Maillist").innerHTML;
        if(Maillist=='无'){
            document.getElementById("MaillistDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("MaillistDiv").style.color="white";
        }
    }else{
        document.getElementById("MaillistDiv").style.backgroundColor="#fe4f5b";
        document.getElementById("MaillistDiv").style.color="white";
    }

    //使用设备数 判断显示不同颜色
    var linkDeviceCountYuanSu=document.getElementById("linkDeviceCount");
    if(linkDeviceCountYuanSu){
        var linkDeviceCount=document.getElementById("linkDeviceCount").innerHTML;
        var newlinkDeviceCount=parseInt(linkDeviceCount);
        if(newlinkDeviceCount>2){
            document.getElementById("linkDeviceCountDiv").style.backgroundColor="#fe4f5b";
            document.getElementById("linkDeviceCountDiv").style.color="white";
        }
    }



    //账户余额 判断显示不同颜色
    var curBalanceYuanSu=document.getElementById("curBalance")
    if(curBalanceYuanSu){
        var curBalance=document.getElementById("curBalance").innerHTML;
        var index= curBalance.indexOf('元');
        curBalance=curBalance.substring(0,index);
        var newCurBalance=parseInt(curBalance);
        if(newCurBalance>0){
            document.getElementById("divOne").style.backgroundColor="#F2F2F2";
            document.getElementById("divOne").style.color="#333333";
        }else{
            if(Math.abs(newCurBalance)>=150){
                document.getElementById("divOne").style.backgroundColor="#fe4f5b";
                document.getElementById("divOne").style.color="white";
            }else{
                document.getElementById("divOne").style.backgroundColor="#f2af0e";
                document.getElementById("divOne").style.color="white";
            }

        }
    }else{
        document.getElementById("divOne").style.backgroundColor="#fe4f5b";
        document.getElementById("divOne").style.color="white";
    }
});

