#!/usr/bin/ruby
ENV['honery_birthday']=Time.new(Time.now.strftime("%Y").to_i,9,20).to_i.to_s;

exit unless `who -q`.include? "xxlv"

def say_baby_i_love_u;  end
def give_her_a_supprise; end
def call_her_and_say_i_miss_u; end
def give_her_a_supprise_again; end
def just_say_something; end
def send_message(message,phone)
    puts "send to #{phone} with message #{message} at time #{Time.now}"
end
def get_birthday_by_phone(phone)
    Time.now.to_i.to_s
end
def get_my_friends
    {
        :nickname=>'PHONE_NUMBER'
    }
end
def get_my_special_friends
    {
        :special_name=>"PHONE_NUMBER"
    }
end
def open_brower_and_just_buy_some_thing_as_you_know
    `open /Applications/Google\ Chrome.app/  https://www.taobao.com?search=what_gift_girl_like?`
end
#crontab * * * * * ruby happy_birthday.rb
exit unless `who -q`.include? "xxlv"
my_friends=get_my_friends
my_special_friends=get_my_special_friends
my_parents={ :dad=>ENV['dad_phone'],:mom=>ENV['mom_phone']}
my_girl_friend= { :honey=>ENV['honery_phone_number'] }
#generate a random message for my friends's birthday
random_happy_message=[
    "好久不见，马上就是你生日，生日快乐！"
    "生日快乐!",
    "What a nice day ,happy to you my best friend,happy birthday!",
    "Hello,long time no see, happy birthday to you!",
].sample
#select a gift for my girl
open_brower_and_just_buy_some_thing_as_you_know if ENV['honery_birthday'].to_i - Time.now.to_i <= 5*24*60*60
say_baby_i_love_u             if ENV['honery_birthday'].to_i - Time.now.to_i <= 4*24*60*60
give_her_a_supprise           if ENV['honery_birthday'].to_i - Time.now.to_i <= 3*24*60*60
call_her_and_say_i_miss_u     if ENV['honery_birthday'].to_i - Time.now.to_i <= 2*24*60*60
give_her_a_supprise_again     if ENV['honery_birthday'].to_i - Time.now.to_i <= 1*24*60*60
just_say_something            if ENV['honery_birthday'].to_i - Time.now.to_i <= 0*24*60*60
#check birthday and send a message
my_friends.each do |nickname,phone|
    birthday=get_birthday_by_phone phone
    send_message random_happy_message,phone if birthday.to_i - Time.now.to_i <= 1*24*60*60
end
#to my parents
my_parents.each do |name,phone|
    birthday=get_birthday_by_phone phone
    send_message "hi, #{name} ,happy birthday!",phone  if birthday.to_i - Time.now.to_i <= 1*24*60*60
end
