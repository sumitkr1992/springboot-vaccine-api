<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Vaccination Slots</title>
</head>

<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="center" valign="top" bgcolor="#838383"
				style="background-color: #838383;"><br> <br>
				<table width="600" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" valign="top" bgcolor="#d3be6c"
							style="background-color: #66ccff; font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #000000; padding: 0px 15px 10px 15px;">
							
							<div style="font-size: 40px; color:#ffffff;">
							<br>
								<b>Vaccine Availability</b> <br>
							</div>
							
							<div style="font-size: 24px; color: #ffffff;">
								<br> List of Centres For 18+ <br><br>
							</div>
							<div>
							
								<#list vaccines as vaccine>
                                  <b>No. Of Slots : ${vaccine.v_avlcap} | ${vaccine.v_dname} | ${vaccine.v_date} | ${vaccine.v_cname}</b> <br>
                                </#list>
                             
                                <br>
                                <br> <br> <b>${Name}</b><br>${location}<br>
							</div>
						</td>
					</tr>
				</table> <br> <br></td>
		</tr>
	</table>
</body>
</html>