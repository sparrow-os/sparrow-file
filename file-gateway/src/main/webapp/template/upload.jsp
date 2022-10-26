<%@ page pageEncoding="UTF-8" %>
<%@taglib uri="http://www.sparrowzoo.com/ui" prefix="j" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>upload demo-${website_name}</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <j:style href="$resource/styles/sparrow.css"/>
    <j:script src="$resource/scripts/require.js"/>
    <j:style href="$resource/styles/pure-css/pure.css"/>
    <!--[if lte IE 8]>
    <j:style href="$resource/styles/layouts-old-ie.css"/>
    <![endif]-->
    <script type="text/javascript">

        requirejs.config({
            baseUrl: "${resource}/scripts",
            paths: {
                menu: '${resource}/scripts/system/menu'
            }
        });
        require(['sparrow', 'domReady!'], function ($, dom) {
            var pathKeySuffixPair = {
                'resource': 'ResourceIco',
                'resource_cover': 'ResourceCoverIco',
                'group': 'GroupIco',
                'cms_cover':'CmsCover',
                'user':'User'
            };
            $.url.upload = $.url.root;
            $.file.isShowProgress = true;
            $.file.initImageUploadEvent('resource', pathKeySuffixPair);
            $.file.initImageUploadEvent('resource_cover', pathKeySuffixPair);
            $.file.initImageUploadEvent('group', pathKeySuffixPair);
            $.file.initImageUploadEvent('cms_cover', pathKeySuffixPair);
            $.file.initImageUploadEvent('user', pathKeySuffixPair);

            document.domain = $.browser.cookie.root_domain;
        });
    </script>
</head>
<body>


resource :${resource}
<H2>forum</H2>
<iframe id="null.resource" class="file-frame" frameborder="0"
        src=""></iframe>

<div id="divResourceIco"></div>

<H2>resource_cover</H2>
<iframe id="null.resource_cover" class="file-frame" frameborder="0"
        src=""></iframe>

<div id="divResourceCoverIco"></div>


<h2>group ICON</h2>
<div id="divGroupIco"></div>
<iframe id="null.group" class="file-frame" frameborder="0"
        src=""></iframe>


<h2>CMS Cover</h2>
<div id="divCmsCover"></div>
<iframe id="null.cms_cover" class="file-frame" frameborder="0"
        src=""></iframe>


<h2>User</h2>
<div id="divUser"></div>
<iframe id="null.user" class="file-frame" frameborder="0"
        src=""></iframe>
</body>
</html>


