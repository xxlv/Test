<?php


class HttpClient{


    protected $_useragent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.101 Chrome/45.0.2454.101 Safari/537.36';
    protected $_sslVerify=false;
    protected $_url;
    protected $_followlocation;
    protected $_timeout;
    protected $_maxRedirects;
    protected $_cookieFileLocation = './cookie.txt';
    protected $_post;
    protected $_postFields;
    protected $_referer ='';
    protected $_session;
    protected $_webpage;
    protected $_includeHeader;
    protected $_noBody;
    protected $_status;
    protected $_version='1.0';
    protected $_headers=[];
    protected $_saveCookie=true;




    protected $_binaryTransfer;
    public    $authentication = 0;
    public    $auth_name      = '';
    public    $auth_pass      = '';

    /**
     * Init
     *
     * @param bool|false $url
     * @param bool|true $followlocation
     * @param int $timeOut
     * @param int $maxRedirecs
     * @param bool|false $binaryTransfer
     * @param bool|false $includeHeader
     * @param bool|false $noBody
     */
    public function __construct($url=false,$followlocation = true,$timeOut = 30,$maxRedirecs = 4,$binaryTransfer = false,$includeHeader = false,$noBody = false)
    {

        $this->_url = $url;
        $this->_followlocation = $followlocation;
        $this->_timeout = $timeOut;
        $this->_maxRedirects = $maxRedirecs;
        $this->_noBody = $noBody;
        $this->_includeHeader = $includeHeader;
        $this->_binaryTransfer = $binaryTransfer;
        $this->_cookieFileLocation = dirname(__FILE__).'/cookie.txt';

    }


    /**
     * Login
     *
     * @param $post_data
     * @return HttpClient
     */
    public function login($login_url,$post_data)
    {

        $this->setUrl($login_url);
        $this->setPost(true);
        $this->setPostFields($post_data);

        return $this->doCurl();
    }

    public function get($url=false)
    {
        if($url){
            $this->setUrl($url);
        }
        return $this->doCurl();
    }

    /**
     * Exec cURL
     *
     * @return $this
     */
    protected function doCurl()
    {
        $ch=curl_init();
        curl_setopt($ch,CURLOPT_URL,$this->_url);
        curl_setopt($ch,CURLOPT_HTTPHEADER,array('Expect:'));
        curl_setopt($ch,CURLOPT_TIMEOUT,$this->_timeout);
        curl_setopt($ch,CURLOPT_MAXREDIRS,$this->_maxRedirects);
        curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
        curl_setopt($ch,CURLOPT_FOLLOWLOCATION,$this->_followlocation);
        curl_setopt($ch,CURLOPT_COOKIEFILE,$this->_cookieFileLocation);
        curl_setopt($ch,CURLOPT_USERAGENT,$this->_useragent);
        curl_setopt($ch,CURLOPT_REFERER,$this->_referer);

        if($this->_saveCookie){
            curl_setopt($ch,CURLOPT_COOKIEJAR,$this->_cookieFileLocation);
        }
        if($this->_headers){
            curl_setopt($ch,CURLOPT_HTTPHEADER,$this->_headers);
        }
        if(!$this->_sslVerify){

            curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,false);
            curl_setopt($ch,CURLOPT_SSL_VERIFYHOST,false);

        }
        if($this->authentication == 1){
            curl_setopt($ch, CURLOPT_USERPWD, $this->auth_name.':'.$this->auth_pass);
        }
        if($this->_post){
            curl_setopt($ch,CURLOPT_POST,true);
            curl_setopt($ch,CURLOPT_POSTFIELDS,$this->_postFields);
        }
        if($this->_includeHeader){
            curl_setopt($ch,CURLOPT_HEADER,true);
        }
        if($this->_noBody){
            curl_setopt($ch,CURLOPT_NOBODY,true);
        }
        if($this->_version=='1.1'){
            curl_setopt($ch,CURLOPT_HTTP_VERSION,CURL_HTTP_VERSION_1_1);
        }else{
            curl_setopt($ch,CURLOPT_HTTP_VERSION,CURL_HTTP_VERSION_1_0);
        }

        $this->_webpage = curl_exec($ch);
        $this->_status = curl_getinfo($ch,CURLINFO_HTTP_CODE);
        curl_close($ch);

        return $this;
    }

    /**
     * @param string $useragent
     */
    public function setUseragent($useragent)
    {
        $this->_useragent = $useragent;
    }

    /**
     * @param mixed $url
     */
    public function setUrl($url)
    {
        $this->_url = $url;
    }

    /**
     * @param boolean $followlocation
     */
    public function setFollowlocation($followlocation)
    {
        $this->_followlocation = $followlocation;
    }

    /**
     * @param int $timeout
     */
    public function setTimeout($timeout)
    {
        $this->_timeout = $timeout;
    }

    /**
     * @param int $maxRedirects
     */
    public function setMaxRedirects($maxRedirects)
    {
        $this->_maxRedirects = $maxRedirects;
    }

    /**
     * @param string $cookieFileLocation
     */
    public function setCookieFileLocation($cookieFileLocation)
    {
        $this->_cookieFileLocation = $cookieFileLocation;
    }

    /**
     * @param mixed $post
     */
    public function setPost($post)
    {
        $this->_post = $post;
    }

    /**
     * @param mixed $postFields
     */
    public function setPostFields($postFields)
    {
        $this->_postFields = $postFields;
    }

    /**
     * @param string $referer
     */
    public function setReferer($referer)
    {
        $this->_referer = $referer;
    }

    /**
     * @param mixed $session
     */
    public function setSession($session)
    {
        $this->_session = $session;
    }

    /**
     * @param mixed $webpage
     */
    public function setWebpage($webpage)
    {
        $this->_webpage = $webpage;
    }

    /**
     * @param boolean $includeHeader
     */
    public function setIncludeHeader($includeHeader)
    {
        $this->_includeHeader = $includeHeader;
    }

    /**
     * @param boolean $noBody
     */
    public function setNoBody($noBody)
    {
        $this->_noBody = $noBody;
    }

    /**
     * @param mixed $status
     */
    public function setStatus($status)
    {
        $this->_status = $status;
    }

    /**
     * @param boolean $binaryTransfer
     */
    public function setBinaryTransfer($binaryTransfer)
    {
        $this->_binaryTransfer = $binaryTransfer;
    }
    /**
     * @param string $version
     */
    public function setVersion($version)
    {
        $this->_version = $version;
    }

    /**
     * @param array $headers
     */
    public function setHeaders($headers)
    {
        $this->_headers = $headers;
    }

    /**
     * @param boolean $saveCookie
     */
    public function setSaveCookie($saveCookie)
    {
        $this->_saveCookie = $saveCookie;
    }
    /**
     * @return mixed
     */
    public function getWebpage()
    {
        return $this->_webpage;
    }

}


