# TODO 移动到环境变量
console.log process.env.HOME

# # Gen a Mail HTML body
# genPushMailBody=(manager,branch,commitHash,reasons=[])->
#     """
#     <div style="font-family: 'lucida Grande', Verdana; line-height: normal;">
#     <sign signid="0"><div style="color: rgb(144, 144, 144); font-family: 'Arial Narrow'; font-size: 12px;">
#     <div style="color: rgb(0, 0, 0); font-family: 'lucida Grande', Verdana; font-size: 14px; line-height: normal;">
#     Hi #{manager}：</div><div style="color: rgb(0, 0, 0); font-family: 'lucida Grande', Verdana; font-size: 14px; line-height: normal;"><sign signid="0"><div style="line-height: normal;"><b><br></b></div><div><sign signid="0">
#     <div style="line-height: normal;">virtual station 版本更新，需要发布，<span style="line-height: 1.5;">
#     当前开发分支位于：<b>#{branch}</b></span><span style="line-height: 1.5;">。</span></div>
#     <div style="line-height: normal;"><br></div>
#     <div style="line-height: normal;">commit :#{commitHash}</div>
#     <div style="line-height: normal;"><br></div>
#     <div><br></div><div style="line-height: normal;">
#     <b>更新摘要</b>：</div><div style="line-height: normal;"><br></div>
#     #{("<div style='line-height: normal;'>－ #{item}</div>" for item in reasons ).join('<br/>')}
#     </sign></div></sign></div></div></sign></div>
#     """
#
# # resport send status
# report_staus=(error,info)->
#     console.log(info)
#     console.log("发送邮件失败！"+ error) if error
#
#
# # Send Mail
# sendmail=(f,to,body='',html='')->
#
#     user=MAIL_USER
#     pass=MAIL_PASS
#     nodemailer=require "nodemailer"
#     subject="VS线上版本发布!!!"
#
#     mailoptions=
#         from :f,
#         to:to,
#         subject:subject,
#         text:body,
#         html:html
#
#     simpleconfig=
#         host:"smtp.exmail.qq.com",
#         port:465,
#         secure:true,
#         auth:
#             user:user,
#             pass:pass
#
#     transporter=nodemailer.createTransport(simpleconfig)
#
#     transporter.sendMail mailoptions,(error,info)->
#         report_staus error,info
#
#
#
# # --------------------------------------------------------
# # ------------------------ 发送邮件------------------------
# # --------------------------------------------------------
#
# from ='lvxx@dxy.cn'
# to = 'lvxiang119@gmail.com'
# body=''
# manager='明哥'
# branch='master'
# commitHash='Hssh'
# reason=["A","B","C"]
#
# html=genPushMailBody manager,branch,commitHash,reason
# sendmail from,to,body,html

# i=Math.floor((Math.random * 1000))%(parseInt(121))

# console.log i
# console.log (Math.random() * 1000)
