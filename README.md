# HTML Parser/Viewer

Based off the Java getting started app for Heroku for ease of deployment to Heroku.
https://github.com/heroku/java-getting-started


## Usage

Simple site for viewing and HTML from a web page.

Uses Heroku for easy deployment.
Sparks for simple java web framework.
JQuery and Bootstrap for JS and CSS. (pulls from CDN.  No local copy)
FreeMarker Java Template Engine for View.


## Some design choices
It will highlight tags that are in the html, script, etc. but only valid tags.

It pulls the list of all valid tags from the Jericho HTML Parsers library. Although not complete, this is extensible if you want to add more.

Parsing was done looking through the returned data character by character.  Any sort of html parsing engine agressively tried to fix bad HTML, which would probably negate the point of examining it.
