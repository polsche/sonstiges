<!DOCTYPE html>
<html>
<head>
    <style>
    .sansserif {
        font-family: Arial, Helvetica, sans-serif;
    }
    .sansserif-bold {
        font-family: Arial, Helvetica, sans-serif;
        font-weight: bold;
    }
    </style>
    <meta charset="UTF-8">
    <title>Infosysbub Studisu Status</title>
</head>
<body>

<h1 class="sansserif">Infosysbub studisu REST API Status</h1>
<br>
<p class="sansserif">Project Name: @project.name@</p>
<p class="sansserif">Project ArtifactId: @project.artifactId@</p>
<p class="sansserif">Project Version: @project.version@</p>
<hr/>
<br><br>

<table>
        <tr>
            <td class="sansserif-bold">Name</td>
            <td class="sansserif-bold">Value</td>
        </tr>
        <#list props?keys as prop>
        <tr>
            <td class="sansserif">${prop}</td>
            <td class="sansserif">${props[prop]}</td>
        </tr>
    </#list>
</table>
</body>
</html>

