
/* THEME CSS*/
/* themeStandard.css */

html, body {
	padding: 0;
	margin: 0;
}
body {
	color: rgb(75, 75, 75);
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	line-height: 1.5;
	background: rgb(255, 255, 255);	
	padding: 0 1em;
}
table {
	font-size: 1em;
}

p, ul, ol, blockquote {
	margin: 0 0 1em 0;
}
p:last-child, ul:last-child, ol:last-child, blockquote:last-child {
	margin-bottom: 0;
}

h1, h2, h3, h4, h5, h6 {
	line-height: 1.2;
	margin: 1em 0 0.5em 0;
}
h1:first-child, h2:first-child, h3:first-child, h4:first-child, h5:first-child, h6:first-child {
	margin-top: 0.67em;
}
h1 {
    font-size: 1.9em;
}
h2 {
    font-size: 1.6em;
}
h3 {
    font-size: 1.2em;
}
h4 {
    font-size: 1.1em;
}
h5 {
    font-size: 1em;
}
h6 {
    font-size: 1em;
}

ul {
	list-style: disc;
}
ol {
	list-style: decimal;
}
ul, ol {
	margin-left: 1.5em;
	padding-left: 1.5em;
}

pre, tt, code {
	font: 11px/1.5 Monaco, Lucidatypewriter, "Lucida console", monospace;
}

textarea, input {
	font: 12px/1.5 Arial, Helvetica, sans-serif;
}

img {
	border-style: solid;
	border-width: 0;
}

a { 
	color: #000099;
	text-decoration: none; 
} 

a:visited {
	color: #660099;
	text-decoration: none;
} 

a.pplink img {
	margin-right: .5em;
    text-decoration: none;
    vertical-align: middle;
}

.pp-image-wrapper {
	font-size: 0.9em;
	line-height: 1.2;
	text-align: left;
	display: inline-block;
	background: rgb(239, 239, 239);
	padding: 0.5em;
	margin: 0 1em 1em 1em;
	border: 1px solid rgb(224, 224, 224);
	border-radius: 4px;
	overflow: hidden;
}
.pp-image-wrapper > img {
	border-radius: 2px;
	margin-bottom: 0.5em;
	display: block;
	clear: both;
}
.pp-image-caption {
	display: block;
	clear: both;
}

.planningDocumentPreview h3 {
	margin: 1em  0 0.5em 0;
}
.planningDocumentPreview ul,
.planningDocumentPreview ol,
.planningDocumentPreview p {
	margin-top: 0;
}

@media print {
	body, pre, textarea, tt, code, select, input {
		color: rgb(0, 0, 0);
		font-size: 10pt;
	}
	a {
		color: rgb(0, 0, 0);
		text-decoration: underline;
	}
}

/* CUSTOM CSS*/



