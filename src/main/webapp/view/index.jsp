<!DOCTYPE html>
<html>
<head>
    <title>Резюме Екатеринбург</title>

    <meta charset="utf-8"/>

    <link href="css/all.css" rel="stylesheet" media="screen"/>

    <script src="js/lib/template.js"></script>
    <script src="js/lib/service.js"></script>
</head>
<body>
<div class="box">
    <div>
        <input type="button" id="startParser" value="Начать парсинг" onclick="startParser()" class="left inner"/>
        <b id="parserWork" class="left inner red">Парсер находится в работе</b>
    </div>
    <div>
        <input type="text" name="search" id="searchField" placeholder="Поиск по заголовоку и описанию" class="left innerInput">
        <input type="button" value="Поиск" onclick="search()" class="left inner">
    </div>
    <div id="searchResults"></div>
</div>

</body>

<script src="js/app.js"></script>

</html>