<html>
<body>
<h2>Hello World!</h2>

<form name="form" action="/manage/product/upload" method="post" enctype="multipart/form-data">

    <input type="file" name="uploadFile" />
    <input type="submit" value="上传文件" />

</form>

<form name="form" action="/manage/product/richupload" method="post" enctype="multipart/form-data">

    <input type="file" name="uploadFile" />
    <input type="submit" value="上传富文本文件" />

</form>

</body>
</html>
