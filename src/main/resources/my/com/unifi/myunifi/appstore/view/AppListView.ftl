<#include "BaseView.ftl">

<#macro content>
<h1>App Store</h1>
<ul>
            <#list apps?keys as appid>
    <li><a href="<@relpath 1 />${appid}">${apps[appid].name}</a></li>
            </#list>
    </ul>
</#macro>

<@html 'App Store' />
