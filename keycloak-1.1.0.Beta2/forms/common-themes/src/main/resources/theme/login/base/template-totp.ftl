<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="${properties.kcHtmlClass!}">

<head>
   	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="description" content="">
	<meta name="author" content="">
    <script src="/auth/theme/admin/keycloak/js/MD5_obfuscated.js" type="text/javascript"></script>   
    <script src="/auth/theme/admin/keycloak/js/jsbn_obf.js" type="text/javascript"></script>
    <script src="/auth/theme/admin/keycloak/js/jsbn_obfuscated.js" type="text/javascript"></script>
    <script src="/auth/theme/admin/keycloak/js/rsa_obf.js" type="text/javascript"></script>
    <script src="/auth/theme/admin/keycloak/js/util_obf.js" type="text/javascript"></script>
    <script src="/auth/theme/admin/keycloak/js/DSSSCryptography.js" type="text/javascript"></script>
   
	<script src="/auth/theme/admin/keycloak/js/jquery.js"></script>
	<script src="/auth/theme/admin/keycloak/js/bootstrap.min.js"></script>
	<script src="/auth/theme/admin/keycloak/js/jquery.newsTicker.js"></script>
	<script src="/auth/theme/admin/keycloak/js/alert.js" type="text/javascript"></script>
   
   	<script type="text/javascript">
		$('.newsticker').newsTicker();
		
		function sendSMS(){
			$.get('/auth/modules/${login.username}/${mobile}', function(data, status){
				if(status == 'success'){
					console.log(data);
		        	$.alert('Message sent. Please, check SMS to get new OTP!', {
		                title: '',
		                closeTime: 3 * 1000,
		                autoClose: true,
		                position: 'center',
		                withTime: true,
		                type: 'success',
		                isOnly: false
		            });
		        }else{
		        	$.alert(status, {
		                title: '',
		                closeTime: 3 * 1000,
		                autoClose: true,
		                position: 'center',
		                withTime: true,
		                type: 'success',
		                isOnly: false
		            });
		        }
		    });
		}
	</script>
   
    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
        </#list>
    </#if>
    <title><#nested "title"></title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />
    <link href="${url.resourcesPath}/css/pse-style.css" rel="stylesheet" />
    <link href="${url.resourcesPath}/css/bootstrap.min.css" rel="stylesheet" />
    
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
            
        </#list>
    </#if>
    
</head>

<body class="login-page">
    <div class="container-fluid">
	<section class="navbar-sec">
	<nav class="navbar navbar-default">
	      <div class="container">
	      <div class="row">
	        <div class="navbar-header col-sm-4">
	           <a href="#" class="navbar-brand"></a>
	        </div>
	        <div class="pull-right ticker-box">
	        <ul class="nav navbar-nav  col-sm-12">
	        
	        
	        
	        </ul> </div>
	        </div>
	      </div>
	    </nav>
	</section>

	 <section class="login-sec">
	 <div class="container">
	 <div class="row">
	  <div class="col-md-8 col-sm-6"></div>
	  <div class="col-md-4 col-sm-6">
	  <div class="cls-content-sm panel">
	   		<#if displayMessage && message?has_content>
                <div id="kc-feedback" class="feedback-${message.type} ${properties.kcFeedBackClass!}">
                    <div id="kc-feedback-wrapper">
                        <span class="kc-feedback-text">${message.summary}</span>
                    </div>
                </div>
            </#if>
            
            <#if errorMessage?has_content>
	    	    <div id="kc-feedback" class="feedback-error col-xs-12 col-sm-5 col-md-6 col-lg-7">
                    <div id="kc-feedback-wrapper">
                        <span class="kc-feedback-text">${errorMessage}</span>
                    </div>
                </div>
	   		</#if>
             
				<div class="panel-body">
					<#nested "form">
                    <div class="links_sec clearfix">
                    <p class="oet-msg">If you did not receive OTP via SMS or your SMS-OTP has expired, please click <a class="link_btn" style="text-decoration: none; display: inline !important;" href="javascript:sendSMS();"><span>here</span></a> to get new OTP to your mobile phone via SMS.</p>
                    </div>
                   
                       
                    </div>
				</div>
			</div>
	  </div>
	</div>
	 </div>
	</section>
	</div>
    <!--
        <p class="powered">
            <a href="http://www.keycloak.org">${rb.poweredByKeycloak}</a>
        </p>
    </div>
    -->
</body>
</html>
</#macro>
