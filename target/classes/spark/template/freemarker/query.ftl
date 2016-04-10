<#assign content>




<div id="opener" class="title">
 <br>
<section class="a">
 <h1><center> <font face="Baskerville" size="10"> Autocorrect Text </font></center></h1>
  </section>
  <br>

</div> <!--opener-->

<center>
<section class="c">


${content}

<form class="pure-form pure-form-stacked">
<div class="pure-control-group">
  <input type="text" name = "line" id="line" size="20" placeholder="Type away!">

</div>
<select id="suggestions" >
<option id="header"> Click for suggestions </option>
  </select>
</section>
</form>


</center>
</form>


</#assign>
<#include "main.ftl">
