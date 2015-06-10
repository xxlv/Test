<?php
/**
 *
 * 日期相关方法
 * @auth xxlv
 */
/**
 * 获取今天的开始跟结束时间戳
 * @return array
 */
function getTodayEndAndBeginTime(){
    $beginToday=mktime(0,0,0,date('m'),date('d'),date('Y'));
    $endToday=mktime(0,0,0,date('m'),date('d')+1,date('Y'))-1;
    return array($beginToday,$endToday);

}
function aweek($gdate = "", $first = 0){

    if(!$gdate) $gdate = date("Y-m-d");
    $w = date("w", strtotime($gdate));//取得一周的第几天,星期天开始0-6
    $dn = $w ? $w - $first : 6;//要减去的天数
    //本周开始日期
    $st = date("Y-m-d", strtotime("$gdate -".$dn." days"));
    //本周结束日期
    $en = date("Y-m-d", strtotime("$st +6 days"));
    //上周开始日期
    $last_st = date('Y-m-d',strtotime("$st - 7 days"));
    //上周结束日期
    $last_en = date('Y-m-d',strtotime("$st - 1 days"));
    return array($st, $en,$last_st,$last_en);//返回开始和结束日期

}

function getWeekStartAndEnd ($year,$week=1) {
    header("Content-type:text/html;charset=utf-8");
    date_default_timezone_set("Asia/Shanghai");
    $year = (int)$year;
    $week = (int)$week;
    //按给定的年份计算本年周总数
    $date = new DateTime;
    $date->setISODate($year, 53);
    $weeks = max($date->format("W"),52);
    //如果给定的周数大于周总数或小于等于0
    if($week>$weeks || $week<=0){
        return false;
    }
    //如果周数小于10
    if($week<10){
        $week = '0'.$week;
    }
    //当周起止时间戳
    $timestamp['start'] = strtotime($year.'W'.$week);
    $timestamp['end'] = strtotime('+1 week -1 day',$timestamp['start']);
    //当周起止日期
    $timeymd['start'] = date("Y-m-d",$timestamp['start']);
    $timeymd['end'] = date("Y-m-d",$timestamp['end']);
    
    //返回起始时间戳
    return $timestamp;
    //返回日期形式
    // return $timeymd;
 }
