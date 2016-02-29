<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Gooru Document Viewer</title>
  <style>
    .pdfpage { position:relative; top: 0; left: 0; border: solid 1px black; margin: 10px; }
    .pdfpage > canvas { position: absolute; top: 0; left: 0; }
    .pdfpage > div { position: absolute; top: 0; left: 0; }
    .inputControl { background: transparent; border: 0px none; position: absolute; margin: auto; }
    .inputControl[type='checkbox'] { margin: 0px; }
    .inputHint { opacity: 0.2; background: #ccc; position: absolute; }
  </style>
</head>

<body>
	<div id="viewer"></div>
</body>
</html>
