<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- Copyright 2006 Idega
     author: Vytautas Čivilis
    -->

    <!-- ####################################################################################################### -->
    
    <!-- This stylesheet copies html form components from chiba generated xhtml document -->

    <!-- ####################################################################################################### -->
    
    
      <xsl:template match="/">
				<xsl:apply-templates select="html/body/form/fieldset" />
			</xsl:template>

			<xsl:template match="html/body/form/fieldset">
				<form_components>
					<xsl:copy-of select ="div[starts-with(@id, 'fbcomp_')]" />
				</form_components>
			</xsl:template>
</xsl:stylesheet>
