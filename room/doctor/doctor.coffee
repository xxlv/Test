http=require 'http'
cheerio=require 'cheerio'
mysql=require 'mysql'
fs=require 'fs'

# host='222.161.191.11'
# http.get {host:host,port:8090,path:'/archive/browse/basicinfo_detail.do?$forward=browse&objectid='+id} , (res)->
#     res.on 'data', (chunck)->
#         data =data+chunck.toString()
#
#     res.on 'end',()->
#         console.log data


conn=mysql.createConnection({
        host:'localhost'
        user:'root'
        password:""
        database:"dts"
    })
conn.connect()

deal=(data,url)->
    $=cheerio.load data

    img=$('#img').text()
    namepinyincode=$('#namepinyincode').val()
    streetname=$('#streetname').val()
    residentcommitteename=$('#residentcommitteename').val()
    addr=$('#addr').val()
    tel=$('#tel').val()
    fatherbirthland=$('#fatherbirthland').val()
    duty_doctor=$('#duty_doctor').val()
    archstatus=$('#archstatus').val()
    cruser=$('#cruser').val()
    build_date=$('#build_date').val()
    icpname=$('#icpname').val()
    archid=$('#archid').val()
    gender=$("input[name='gender']").val()
    identityno=$("input[name='identityno']").val()
    birthday=$("input[name='birthday']").val()
    company=$("input[name='company']").val()
    linkman=$("input[name='linkman']").val()
    linkman_tel=$("input[name='linkman_tel']").val()
    residenttype=$("input[name='residenttype']").val()
    nation=$("input[name='nation']").val()
    bloodgroup=$("input[name='bloodgroup']").val()
    bloodRH=$("input[name='bloodRH']").val()
    education=$("input[name='education']").val()
    vocation=$("input[name='vocation']").val()
    marriage=$("input[name='marriage']").val()
    medicare_type=$("input[name='medicare_type']").val()
    paytype_checkbox=$('#paytype_checkbox').text()
    medicareid=$("input[name='medicareid']").val()
    familyid=$("input[name='familyid']").val()
    familyhead=$("input[name='familyhead']").val()
    familypositionhidden=$("input[name='familypositionhidden']").val()
    archiveno=$("input[name='archiveno']").val()
    remark=$("input[name='remark']").val()

    sql="INSERT INTO infomation VALUES (NULL,
    '#{img}',
    '#{namepinyincode}',
    '#{streetname}',
    '#{residentcommitteename}',
    '#{addr}',
    '#{tel}',
    '#{fatherbirthland}',
    '#{duty_doctor}',
    '#{archstatus}',
    '#{cruser}',
    '#{build_date}',
    '#{icpname}',
    '#{archid}',
    '#{gender}',
    '#{identityno}',
    '#{birthday}',
    '#{company}',
    '#{linkman}',
    '#{linkman_tel}',
    '#{residenttype}',
    '#{nation}',
    '#{bloodgroup}',
    '#{bloodRH}',
    '#{education}',
    '#{vocation}',
    '#{marriage}',
    '#{medicare_type}',
    '#{paytype_checkbox}',
    '#{medicareid}',
    '#{familyid}',
    '#{familyhead}',
    '#{familypositionhidden}',
    '#{archiveno}',
    '#{remark}'
    );"

    if namepinyincode != 'undefined' && namepinyincode?
        conn.query sql,(e,r)->
            if e
                fs.writeFile '/work/learn/Test/room/doctor/err.log', 'current id is ', (e)->
                    console.log 'Error to W'
                throw e
            console.log "Done #{url}"

    else
        console.warn  "SKIP #{url}"

# Spider
sp=(url)->
    http.get url,(res)->
        data=''
        console.log res.data
        res.on 'data',(chunck)->
            data+=chunck
        res.on 'end' ,()->
            deal data,url

        res.on 'error',(e)->
            console.log e



do ()->
    o_url="http://222.161.191.11:8090/archive/browse/basicinfo_detail.do?$forward=browse&objectid="
    id=474659
    setInterval ->
        url=o_url+id
        sp(url)
        # console.log "load url #{url}"
        id++
      , 100
