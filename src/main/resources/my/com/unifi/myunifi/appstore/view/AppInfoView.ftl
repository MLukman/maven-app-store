<#include "BaseView.ftl">

<#macro content>
<h1>${appConfig.name}</h1>
<div>Updated ${maven.updated?string("yyyy-MM-dd h:mm a Z")}</div>

<h3><a href="<@relpath 1 />${appId}/dl">Download latest (${maven.latest})</a></h3>

<h2>All versions</h2>
<ul>
    <#list maven.versions as version>
    <li><a href="<@relpath 1 />${appId}/${version}">${version}</a></li>
    </#list>
    </ul>
</#macro>

<@html appConfig.name />
