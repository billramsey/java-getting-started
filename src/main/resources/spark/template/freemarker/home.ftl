<!DOCTYPE html>
<html>
<head>
  <#include "header.ftl">
  
<script>
  $(document).ready(function() {
  });
  function toggle(param) {
   if ($("#lastToggled").val() != ''){
     $($("#lastToggled").val()).toggleClass( "blue" );
   }
  
   $('.' + param).toggleClass( "blue" )
   $("#lastToggled").val('.' + param);
  }
  
  
</script>  
</head>

<body>


<br />

<form name="url" action="/" method="POST">
  <input type="hidden" id="lastToggled" value="">
    <div class="container">
        <div class="jumbotron">
          <h3>Source Code Viewer</h3>
          <p><p/>
          <div class="input-group">
            <span class="input-group-addon" id="basic-addon3">URL to examine goes here.</span>
            <input type="text" class="form-control" name="address" value="${address!"https://www.google.com"}"
            aria-describedby="basic-addon3">
                  <span class="input-group-btn">
                  <button class="btn btn-default" type="submit">Go!</button>
                </span>
          </div>
        </div>
    </div>
</form>



<hr>
<#if count??>

<div class="container">
  <div class="row">
      <div class="col-md-3">
        <ul class="list-group">
        <#assign i=0>
          <#list count?keys?sort as prop>
          
             <li class="list-group-item"> 
             <a class="tag_link" onclick="toggle('tag_toggle_${prop}')">${prop}</a>
              <span class="badge">
              ${count[prop]}
              </span>
              </li>
              <#assign i = i + 1>
              <#if i % 10 == 0>
         </ul>
      </div>
      <div class="col-md-3">
        <ul class="list-group">
              </#if>
              
              
          </#list>
        </ul>
      </div>
  </div>
</div>
</#if>

<p class="htmlview">

${source}

</p>



</body>
</html>