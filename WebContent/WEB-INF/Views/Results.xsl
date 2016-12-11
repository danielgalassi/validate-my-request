<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes"/>
	<xsl:template match="/">
	<!--xsl:param name="SelectedSubjectArea"/-->
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<style type="text/css">
		h1 {font-family: Futura, "Trebuchet MS", Arial, sans-serif; font-size: 24pt; color: #676767;}
		h2 {font-family: Futura, "Trebuchet MS", Arial, sans-serif; font-size: 20pt; color: #777777;}
		h3 {font-family: Futura, "Trebuchet MS", Arial, sans-serif; font-size: 16pt; color: #777777;}
		h4 {font-family: Futura, "Trebuchet MS", Arial, sans-serif; font-size: 12pt; color: #888888;}
		li {font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size: 10pt; color: #474747;}
		a  {font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size: 10pt; color: #444444;}
		*  {font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size:  8pt; color: #333333;}
		p  {font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size: 10pt; color: rgb(128, 128, 128);}
		tr {height:30; font-size: 8.5pt;}
		tr:hover {background: rgb(248,248,248);}
		table {
			border-spacing: 0 0;
			margin: 1px;
			border-right: 1px solid #DEDEDE;
			font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size: 8.5pt;
			}
		th	{
			font-family: Helvetica, sans-serif;
			font-size: 9pt;
			font-weight: bold;
			text-align:center;
			background: #ECECEC;
			color: #555555;
			border-left: 1px solid #DEDEDE;
			border-top: 1px solid #DEDEDE;
			border-bottom: 1px solid #DEDEDE;
			}
		tbody td {
				font-family: Helvetica, sans-serif;
				font-size: 8pt;
        		font-weight: bold;
        		text-align:left;
        		color: #444444;
				border-bottom: 1px solid #DEDEDE;
				border-left: 1px solid #DEDEDE;
				padding-left:6px;
				padding-right:6px;
				}
				</style>
				
				<title>Netezza Refresh Request Validator Service Report</title>
			</head>
			<body>
				<!-- Validator Service headings -->
				<h1>Netezza Validator Service Report</h1>
				<br/>
				<h2>Results</h2>
				<div style="padding-top: 0px; padding-bottom: 50px; padding-left: 30px; padding-right: 30px">
					<h3 style="font-size: 30px; color: green; float: left;">
					OK <xsl:value-of select="count(//object[@isValid='true'])"/>
					</h3>
					<h3 style="font-size: 30px; color: red; float: right;">
					ERR: <xsl:value-of select="count(//object[@isValid='false'])"/>
					</h3>
				</div>
				<h2/>
				<br/>
				<br/>
				<table>
					<tbody>
						<tr>
							<th width="250px" height="28px">Object</th>
							<th width="350px" height="28px">Type</th>
							<th width="250px" height="28px">Result</th>
							<th width="450px" height="28px">Comment</th>
						</tr>
						
						<xsl:for-each select="//object">
					<!-- Results Section -->
						<!-- No objects to validate -->
<!--xsl:if test="count(//object) = 0">
<p style="font-weight: bold;">No objects assessed.</p>
</xsl:if-->
						<!-- No errors found (failed or N/A results) -->
						<!--xsl:if test="$ShowErrorsOnly='true' and count(./Results/Object[@result='Fail']) = 0  and count(./Results/Object[@result='N/A']) = 0 and count(./Results/Object) > 0">
							<p style="font-weight: bold;">No failed or inconclusive results.</p>
						</xsl:if-->
						<!-- Table Section -->
					<!--xsl:if test="(count(./Results/Object) > 0 or (count(./Results/Object[@result='Fail']) > 0  or count(./Results/Object[@result='N/A']) > 0))"-->
							<!-- Object Hierarchy across table heading -->
							<tr>
								<td height="28px">
									<xsl:value-of select="normalize-space(.)"/>
								</td>
								<td height="28px">
									<xsl:value-of select="@type"/>
								</td>

								<!-- Test Results Section -->
								<!--xsl:for-each select="./object"-->
								<xsl:if test="(@isValid='true')">
										<!-- Green cells = Pass -->
									<td style="background: #CCFF99; color: green; text-align:center;">OK</td>
									<td style="background: #CCFF99; color: green;">
										<xsl:value-of select="@comment"/>
									</td>
											<!--/xsl:if-->
								</xsl:if>
								<xsl:if test="(@isValid='false')">
										<!-- Red cells = Fail -->
									<td style="background: #FFF1BF; color: red; text-align:center;">Object Not Found</td>
									<td style="background: #FFF1BF; color: red;">
										<xsl:value-of select="@comment"/>
									</td>
								</xsl:if>
							</tr>
								<!--/xsl:for-each-->
					<!--/xsl:if-->
						</xsl:for-each>
					</tbody>
				</table>
				<br/>
				<br/>
				<hr style="height: 1px; border: 0; background-color: #AAAAAA; width: 70%;"/>
				
				<div style="padding-top: 20px;">
					<p style="color: rgb(128, 128, 128); float: left;">Mozilla Firefox or Google Chrome are strongly recommended for best results.</p>
					<p style="color: rgb(128, 128, 128); float: right;">Environment Management Team</p>
				</div>
				<p style="color: rgb(0, 0, 255); float: center;"> This HTML page is W3C compliant.</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
