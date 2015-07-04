---
layout: post
title: New Post
permalink: new-page.html
description: Some Description
date: 2015-06-11 16:46:29 +08:00
tags: "some tags here"
---

#数据侠移动第三方平台API
##管理部门  
####获取部门列表
- 请求说明 Http请求：**GET**
- 路由: /department?token=ACCESS_TOKEN&id=1
-  参数说明：
<table border="1" cellspacing="0" cellpadding="4" align="center" width="640px">
    <tr>
        <td>参数</td><td>必须</td><td>说明</td>
    </tr>
   <tr>
        <td>token</td><td>是</td><td>调用接口凭据</td>
    </tr>
<tr>
        <td>id</td><td>否</td><td>部门id，将获得该id下的所有的子部门</td>
    </tr>
</table>
- 返回结果
    {
    “errcode”:0,
    "errmsg":"ok",
    "department":
    [
    	{
    		"id":2,
    		"name":"数据侠",
    		"parentid":1,
    		"order":100
    	}，
    	{
    		"id":2,
    		"name":"数据侠移动",
    		"parentid":1,
    		"order":100
    		}
    ]} 
- 返回说明

<table border="1" cellspacing="0" cellpadding="4" align="center" width="640px">
<tbody><tr>
<th style="width:150px">参数
</th>
<th>说明
</th></tr>
<tr>
<td> errcode
</td>
<td> 返回码
</td></tr>
<tr>
<td> errmsg
</td>
<td> 对返回码的文本描述内容
</td></tr>
<tr>
<td> department
</td>
<td> 部门列表数据。以部门的order字段从小到大排列
</td></tr>
<tr>
<td> id
</td>
<td> 部门id
</td></tr>
<tr>
<td> name
</td>
<td> 部门名称
</td></tr>
<tr>
<td> parentid
</td>
<td> 父亲部门id。根部门为1
</td>
</tr>

<tr>
<td> order
</td>
<td> 在父部门中的次序值。order值小的排序靠前。
</td></tr>
</tbody></table>

