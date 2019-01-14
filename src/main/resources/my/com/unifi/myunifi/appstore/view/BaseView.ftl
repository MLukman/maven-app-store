<#ftl strip_whitespace = true>

<#macro content></#macro>

<#macro relpath><#list 0..<pathLevel as i>../</#list></#macro>

<#macro html title>
<!DOCTYPE html>
<html>
    <head>
        <title>${title}</title>
        </head>
    <body style="padding:0 10pt">

        <@content />

        </body>
    </html>
</#macro>
