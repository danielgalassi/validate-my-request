<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="xml"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" />
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<style type="text/css">
					h1 {font-family: Futura, "Trebuchet MS", Arial,
					sans-serif;
					font-size: 24pt; color: #676767;}
					h2 {font-family:
					Futura, "Trebuchet MS", Arial, sans-serif;
					font-size: 20pt; color:
					#777777;}
					h3 {font-family: Futura, "Trebuchet MS", Arial,
					sans-serif;
					font-size: 16pt; color: #777777;}
					h4 {font-family:
					Futura, "Trebuchet MS", Arial, sans-serif;
					font-size: 12pt; color:
					#888888;}
					li {font-family: "Helvetica Neue", Helvetica, Arial,
					sans-serif;
					font-size: 10pt; color: #474747;}
					a {font-family:
					"Helvetica Neue", Helvetica, Arial, sans-serif;
					font-size: 10pt;
					color: #444444;}
					* {font-family: "Helvetica Neue", Helvetica, Arial,
					sans-serif;
					font-size: 10pt; color: #333333;}
					p {font-family:
					"Helvetica Neue", Helvetica, Arial, sans-serif;
					font-size: 10pt;
					color: rgb(128, 128, 128);}
					tr {height:30; font-size: 10pt;}
					tr:hover {background: rgb(248,248,248);}
					tbody td:hover {background:
					rgb(248,248,248);}
					table {
					width: 100%;
					border-spacing: 0 0;
					margin:
					2px;
					border-right: 1px solid #DEDEDE;
					font-family: "Helvetica Neue",
					Helvetica, Arial, sans-serif; font-size: 8.5pt;
					}
					th {
					font-family:
					Helvetica, sans-serif;
					font-size: 11pt;
					font-weight: 600;
					text-align:center;
					background: #ECECEC;
					color: #555555;
					border-left:
					1px solid #DEDEDE;
					border-top: 1px solid #DEDEDE;
					border-bottom: 1px
					solid #DEDEDE;
					}
					tbody td {
					font-family: Helvetica, sans-serif;
					font-size: 10pt;
					font-weight: 300;
					text-align: left;
					color: #444444;
					border-bottom: 1px solid #DEDEDE;
					border-left: 1px solid #DEDEDE;
					padding-left: 3px;
					padding-right: 3px;
					}
				</style>

				<title>Netezza Refresh Request Validator Service Report</title>
			</head>
			<body>
				<!-- Validator Service headings -->
				<h1>Netezza Validator Service Report</h1>
				<h2>Results</h2>
				<p style="text-align: center;">
					<xsl:choose>
						<xsl:when test="count(//object[@valid='false']) &gt; 0">
							<svg width="3%" height="3%" viewBox="0 0 341 329" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:cc="http://creativecommons.org/ns#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" id="svg3508" version="1.1" inkscape:version="0.91 r13725" sodipodi:docname="thumb-down.svg"> <metadata id="metadata3518"> <rdf:RDF> <cc:Work rdf:about=""> <dc:format>image/svg+xml</dc:format> <dc:type rdf:resource="http://purl.org/dc/dcmitype/StillImage" /> <dc:title /> </cc:Work> </rdf:RDF> </metadata> <defs id="defs3516" /> <sodipodi:namedview pagecolor="#ffffff" bordercolor="#666666" borderopacity="1" objecttolerance="10" gridtolerance="10" guidetolerance="10" inkscape:pageopacity="0" inkscape:pageshadow="2" inkscape:window-width="1920" inkscape:window-height="1151" id="namedview3514" showgrid="false" inkscape:zoom="2.2784195" inkscape:cx="213.125" inkscape:cy="135.40088" inkscape:window-x="0" inkscape:window-y="25" inkscape:window-maximized="1" inkscape:current-layer="svg3508" /> <path style="fill:#32363b" inkscape:connector-curvature="0" d="m 163.04,329 -3.89,0 c -12.41,-1.17 -23.12,-9.01 -30.4,-18.78 -12.67,-16.89 -18.79,-38.67 -16.47,-59.68 1.74,-11.81 3.65,-23.6 5.41,-35.41 -26.22,-0.09 -52.43,0.01 -78.64,-0.05 C 23.68,214.74 9.03,203.92 4.76,189.06 1.52,178.25 3.98,166.1 10.89,157.23 4.5,150.85 0.8,142.21 0,133.25 l 0,-3.59 C 0.87,118.05 7.18,107.02 17.15,100.89 13.27,90.69 13.57,78.72 19.13,69.19 22.87,62.34 29.09,57.17 36.09,53.89 32.69,40 38.47,24.35 50.31,16.25 56.32,12.03 63.66,9.61 71.04,9.8 112.02,9.83 153,9.79 193.98,9.82 c 12.65,0.42 25.19,5.02 34.98,13.09 0.18,-5.07 -0.87,-10.57 1.88,-15.16 2.25,-4.46 7,-6.98 11.78,-7.75 l 84.6,0 c 7.14,0.76 13,6.63 13.78,13.75 l 0,157.71 c -0.77,7.07 -6.66,13.47 -13.98,13.6 -27.66,0.09 -55.33,0 -83,0.05 -4.02,0.17 -8,-1.53 -10.81,-4.37 -2.38,-2.27 -3.27,-5.52 -4.66,-8.4 -0.97,1.43 -1.95,2.86 -2.79,4.37 -13.87,25.8 -27.81,51.56 -41.69,77.35 -3,5.16 -2.34,11.29 -1.98,16.98 1.31,15.86 2.4,31.73 3.67,47.59 -6.26,5.78 -14.21,9.54 -22.72,10.37 m 7.99,-17.15 c -1.22,-14.98 -2.43,-29.95 -3.4,-44.94 -0.56,-6.69 0.67,-13.53 3.84,-19.47 14.01,-25.96 28,-51.92 42,-77.89 2.9,-7.14 10.3,-10.57 15.43,-15.89 -0.04,-38.11 0.01,-76.22 -0.03,-114.34 -6.74,-2.49 -11.41,-8.34 -17.98,-11.12 -6.23,-2.76 -13.05,-4.44 -19.89,-4.21 -39.64,0.03 -79.29,0.04 -118.93,0 -12.99,-1.18 -24.76,11.66 -22.45,24.48 0.79,5.67 4.24,10.4 6.63,15.46 -5.61,1.42 -11.75,1.24 -16.82,4.34 -7.22,4 -11.53,12.48 -10.56,20.67 0.28,7.61 6.07,13.14 10.26,18.92 -5.99,2.21 -12.8,3.07 -17.63,7.61 -7.76,6.72 -9.5,19.16 -3.71,27.67 3.8,6.29 11.01,8.84 16.98,12.5 -3.28,2.45 -6.6,4.86 -9.77,7.46 -5.93,4.98 -8.74,13.4 -6.87,20.94 1.89,7.69 8.38,13.84 15.93,15.97 3.51,1.19 7.26,0.91 10.91,0.93 29.75,0 59.51,-0.01 89.26,0.01 -2.52,17.32 -5.38,34.6 -7.9,51.92 -1.52,14.44 2.1,29.14 9.02,41.81 4.33,7.63 10.17,15.04 18.48,18.52 5.53,2.39 12.07,1.72 17.2,-1.35 M 326.52,170.63 C 326.44,118.6 326.56,66.57 326.46,14.54 298.78,14.46 271.1,14.5 243.42,14.53 c -0.08,52.06 -0.05,104.12 -0.02,156.18 27.71,0.01 55.42,0.17 83.12,-0.08 z" id="path3510" /> <path style="fill:#758fb5" inkscape:connector-curvature="0" d="m 326.52,170.63 c -27.7,0.25 -55.41,0.09 -83.12,0.08 -0.03,-52.06 -0.06,-104.12 0.02,-156.18 27.68,-0.03 55.36,-0.07 83.04,0.01 0.1,52.03 -0.02,104.06 0.06,156.09 z" id="path3512" /></svg>
						</xsl:when>
						<xsl:otherwise>
							<svg width="3%" height="3%" viewBox="0 0 341 329" xmlns="http://www.w3.org/2000/svg"><path fill="#32363b" d="M177.96 0h3.89c12.41 1.17 23.12 9.01 30.4 18.78 12.67 16.89 18.79 38.67 16.47 59.68-1.74 11.81-3.65 23.6-5.41 35.41 26.22.09 52.43-.01 78.64.05 15.37.34 30.02 11.16 34.29 26.02 3.24 10.81.78 22.96-6.13 31.83 6.39 6.38 10.09 15.02 10.89 23.98v3.59c-.87 11.61-7.18 22.64-17.15 28.77 3.88 10.2 3.58 22.17-1.98 31.7-3.74 6.85-9.96 12.02-16.96 15.3 3.4 13.89-2.38 29.54-14.22 37.64-6.01 4.22-13.35 6.64-20.73 6.45-40.98-.03-81.96.01-122.94-.02-12.65-.42-25.19-5.02-34.98-13.09-.18 5.07.87 10.57-1.88 15.16-2.25 4.46-7 6.98-11.78 7.75h-84.6c-7.14-.76-13-6.63-13.78-13.75V157.54c.77-7.07 6.66-13.47 13.98-13.6 27.66-.09 55.33 0 83-.05 4.02-.17 8 1.53 10.81 4.37 2.38 2.27 3.27 5.52 4.66 8.4.97-1.43 1.95-2.86 2.79-4.37 13.87-25.8 27.81-51.56 41.69-77.35 3-5.16 2.34-11.29 1.98-16.98-1.31-15.86-2.4-31.73-3.67-47.59C161.5 4.59 169.45.83 177.96 0m-7.99 17.15c1.22 14.98 2.43 29.95 3.4 44.94.56 6.69-.67 13.53-3.84 19.47-14.01 25.96-28 51.92-42 77.89-2.9 7.14-10.3 10.57-15.43 15.89.04 38.11-.01 76.22.03 114.34 6.74 2.49 11.41 8.34 17.98 11.12 6.23 2.76 13.05 4.44 19.89 4.21 39.64-.03 79.29-.04 118.93 0 12.99 1.18 24.76-11.66 22.45-24.48-.79-5.67-4.24-10.4-6.63-15.46 5.61-1.42 11.75-1.24 16.82-4.34 7.22-4 11.53-12.48 10.56-20.67-.28-7.61-6.07-13.14-10.26-18.92 5.99-2.21 12.8-3.07 17.63-7.61 7.76-6.72 9.5-19.16 3.71-27.67-3.8-6.29-11.01-8.84-16.98-12.5 3.28-2.45 6.6-4.86 9.77-7.46 5.93-4.98 8.74-13.4 6.87-20.94-1.89-7.69-8.38-13.84-15.93-15.97-3.51-1.19-7.26-.91-10.91-.93-29.75 0-59.51.01-89.26-.01 2.52-17.32 5.38-34.6 7.9-51.92 1.52-14.44-2.1-29.14-9.02-41.81-4.33-7.63-10.17-15.04-18.48-18.52-5.53-2.39-12.07-1.72-17.2 1.35M14.48 158.37c.08 52.03-.04 104.06.06 156.09 27.68.08 55.36.04 83.04.01.08-52.06.05-104.12.02-156.18-27.71-.01-55.42-.17-83.12.08z"/><path fill="#758fb5" d="M14.48 158.37c27.7-.25 55.41-.09 83.12-.08.03 52.06.06 104.12-.02 156.18-27.68.03-55.36.07-83.04-.01-.1-52.03.02-104.06-.06-156.09z"/></svg>
						</xsl:otherwise>
					</xsl:choose>
				</p>
				
				<div
					style="padding-top: 0px; padding-bottom: 50px; padding-left: 30px; padding-right: 30px">
					<h3 style="font-size: 30px; color: green; float: left;">
						OK:
						<xsl:value-of select="count(//object[@valid='true'])" />
					</h3>
					<h3 style="font-size: 30px; color: red; float: right;">
						ERR:
						<xsl:value-of select="count(//object[@valid='false'])" />
					</h3>
				</div>
				<h2 />
				<br />
				<br />
				<table>
					<tbody>
						<tr>
							<th width="35%" height="28px">Object</th>
							<th width="15%" height="28px">Type</th>
							<th width="15%" height="28px">Result</th>
							<th width="35%" height="28px">Comment</th>
						</tr>

						<xsl:for-each select="//object">
							<!-- Results (table) Section -->
							<tr>
								<td height="28px">
									<xsl:value-of select="normalize-space(@fullObject)" />
								</td>
								<td height="28px">
									<xsl:value-of select="@type" />
								</td>

								<!-- Test Results Section -->
								<xsl:if test="(@valid='true')">
									<!-- Green cells = Pass -->
									<td style="background: #CCFF99; color: green; text-align:center;">OK</td>
									<td style="background: #CCFF99; color: green;">
										<xsl:value-of select="@comment" />
									</td>
								</xsl:if>
								<xsl:if test="(@valid='false')">
									<!-- Red cells = Fail -->
									<td style="background: #FFF1BF; color: red; text-align:center;">Object Not Found</td>
									<td style="background: #FFF1BF; color: red;">
										<xsl:value-of select="@comment" />
									</td>
								</xsl:if>
							</tr>
						</xsl:for-each>
					</tbody>
				</table>
				<br />
				<br />
				<hr style="height: 1px; border: 0; background-color: #AAAAAA; width: 100%;" />

				<div style="padding-top: 20px;">
					<p style="color: rgb(128, 128, 128); float: left;">Mozilla Firefox or Google Chrome are strongly
						recommended for best results.
					</p>
					<p style="color: rgb(128, 128, 128); float: right;">Environment + Dev Team</p>
				</div>
				<p style="color: rgb(0, 0, 255); float: center;"> This HTML page is W3C compliant.</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
