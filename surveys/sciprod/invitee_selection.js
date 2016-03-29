arr = [1200,1201,1202]
function checkValue(value)
  {
    var flag = false;
	for(var i=0;i<arr.length;i++)
	  { 
	    if (arr[i]==value)
          {flag=true
		  };
	   } 
	 return flag
	 }

for(var i=0; i < document.form1.length; i++)
 { var condition = checkValue(document.form1.elements[i].value);
   console.log(i + " " + condition);
   if(document.form1.elements[i].type=="checkbox" && document.form1.elements[i].name =="user" && condition) 
     document.form1.elements[i].checked=true; }
