# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
1.times do |i|
    Article.create(title:"TITLE INIT DATA AT #{i}",text:"TEXT at #{i}")
    puts "INSERT title is TITLE INIT DATA AT #{i}  ,text is TEXT at #{i}"
end
