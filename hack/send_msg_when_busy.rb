#!/usr/bin/env ruby
exit unless `who -q`.include? 'xxlv'
require 'twilio-ruby'

#require 'mail'
account_sid = 'ACa5e472c3d10fe19b37f5201fb1fd1c08'
auth_token  = '63320952620328228858f46b05ae8cb5'

my_number  = '+12075187161'
her_number = '+8617086894013'
@client = Twilio::REST::Client.new account_sid, auth_token


reason = [
  '回答在牛奶箱里！',
  '我还有一点事要处理！',
  '我去火星',
  '晚点回去'
].sample


@client.account.messages.create({
	:from => my_number,
    :to=>her_number,
    :body=>"#{reason}"
})
puts "Message sent at: #{Time.now} | Reason: #{reason}"

# smtp = {
#     :address => 'smtp.qq.com',
#     :port => 25, :domain => 'qq.com',
#     :user_name => '1252804799@qq.com',
#     :password => 'PASSWORD',
#     :enable_starttls_auto => true,
#     :openssl_verify_mode => 'none'
# }
# Mail.defaults { delivery_method :smtp, smtp }
# mail = Mail.new do
#   from '1252804799@qq.com'
#   to 'lvxiang119@gmail.com'
#   subject 'New Event Push!'
#   body reason
# end
#
# mail.deliver!
