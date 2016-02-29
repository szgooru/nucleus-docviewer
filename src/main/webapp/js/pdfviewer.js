$(function () {
	var file_extension = ".html";
	var page = 1;
	$('#findbar').hide();
	$('.no_of_pages').text(totalPages);
	$('#previous').attr('disabled','disabled');
	
	for (index = 1; index <= totalPages; index++) { 
 	  $("#thumbnailView").append("<div class='thumbView' id='pages-"+ page + "'><iframe marginwidth=0  marginheight=0 scrolling=no width=100% style='border:0px' height=100% src="+repoBaseFolderUrl + '-' + index + file_extension+"></iframe></div>");
	 
	  $("#pages").append("<div class='page' id='page-"+ page++ + "'><iframe marginwidth=0  marginheight=0 scrolling=no width=100% style='border:0px' height=100% src="+repoBaseFolderUrl + '-' + index + file_extension+"></iframe></div>");
	  
	  
// 	  $("#thumbnailView").append("<canvas class='page' id='page-"+ page++ + "'><iframe marginwidth=0  marginheight=0 scrolling=no width=100% style='border:0px' height=100% src="+repoBaseFolderUrl + '-' + index + file_extension+"></iframe></canvas>");
	}
	  
	$('#pageNumber').on('keypress', function (e) { 
	    var keycode = (e.keyCode ? e.keyCode : e.which);
	    if (keycode == 13) {
	      navigatePage();
	    }	    
	});
// 	$('.splitToolbarButton button').click(function(){
// 	    navigatePage();
// 	});
	 $('#next').click(function(){
	      var pageIndex = (parseInt($("#pageNumber").val()));
	      if(pageIndex<totalPages){
		pageIndex+=1;
	      }
	      $("#pageNumber").val(pageIndex);
	      navigatePage();
	      
	});
	
	  $('#previous').click(function(){
	      var currentPageIndex = $("#pageNumber").val();
	      if (currentPageIndex >1) {
		console.log(currentPageIndex);
		currentPageIndex = currentPageIndex - 1;
		  $("#pageNumber").val(currentPageIndex);
	      } 
	      navigatePage();
	  });
      
      
	function navigatePage() { 
	 //alert("nav");
	    var pageIndex = parseInt($('#pageNumber').val());
	    $("#viewerContainer").scrollTop(($("#page-" + pageIndex).index('.page') * $('.page:eq(0)').height()) + ($("#page-" + pageIndex).index('.page') * 20));
	    buttonEnable();
	}
	function buttonEnable(){
	  var pageIndex = parseInt($('#pageNumber').val());
	  if(pageIndex==totalPages){
		$('#next').attr('disabled','disabled');		
	      $("#previous").removeAttr('disabled');
	  }
	    else if(pageIndex==1){
		  $('#previous').attr('disabled','disabled');
		  $("#next").removeAttr('disabled');
	    }
	    else{
	      $("#next").removeAttr('disabled');
	      $("#previous").removeAttr('disabled');
	    }
	}
	 /*scroll*/
	 $('#viewerContainer').scroll(function(){

	    // console.log($("#page-" + pageIndex).index('.page'));
	      var pageHeight = $('.page:eq(0)').height();
	      var pageIndexHeight = $('#viewerContainer').scrollTop()+800;
	      var pageNo =  Math.round(pageIndexHeight/pageHeight) ;
	      console.log($('#viewerContainer').height());
	      if (pageNo >= 1)
		  $("#pageNumber").val(pageNo);
	      buttonEnable();
	});
	$("#toolbarViewerRight #presentationMode").on('click', function() {
	    var docElement, request;
	    docElement = document.getElementById("viewerContainer"); 
	    //docElement = document.documentElement;
	    request = docElement.requestFullScreen || docElement.webkitRequestFullScreen || docElement.mozRequestFullScreen || docElement.msRequestFullScreen;
	    if(typeof request!="undefined" && request){
		request.call(docElement);
	    }
	});
	
      $('#viewFind').click(function(){
	  $('#findbar').toggle();
	});
            
      $('#sidebarToggle').click(function(){
	  //$('#sidebarContainer').css({'visibility':'visible','left':'0px'});
	  
	if ($("#toolbarSidebar").css('visibility') === 'hidden') {
	      $('#sidebarContainer').css({'visibility':'visible','left':'0px'});
	      $("#viewerContainer").css("margin-left","200px");
	      $("#toolbarSidebar").css({'visibility':'visible','left':'0px'});
	      
	      $("#toolbarContainer").css("margin-left","200px");/*
	      $(".splitToolbarButton .toggled").css({'visibility':'visible','left':'0px'});*/
	      
	      $('#thumbnailView .thumbView').css('transform','scale(' + .1 + ')');
	         
	}else{
	    $('#sidebarContainer').css({'visibility':'hidden','left':'0px'});
	    $("#toolbarContainer").css("margin-left","0px");
	    $("#viewerContainer").css("margin-left","0px");
	    $("#toolbarSidebar").css('visibility','hidden');
	} 
	  
	});
 });



 window.onload = function() {
  var currFFZoom = 1;
  var currIEZoom = 100;  
  var previous=current=step=i=0;
  
  $('#zoomIn').on('click',function(){
	  previous=$('#scaleSelect').val()*100;
	  for(i=1;i<=10;i++){
	      step=(i*100);
		if(previous<step){
		    step=i*10;
		    break;      
		}
	  }
	  
	  current=(previous+step)/100;
	  $('#customScaleOption').show();
	  $('#customScaleOption').empty(); 
	  $('#customScaleOption').val(current);      
	  $('#scaleSelect option[value="'+current+'"]').text(parseInt(previous+(step))+'%');
	  $('#scaleSelect').val(current);
	  
	  if ((navigator.userAgent.indexOf('Chrome') != -1||navigator.userAgent.indexOf('Firefox') != -1) && (
	  parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Chrome') + 8)) >= 4.6 || parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Firefox') + 8)) >= 4.6)){//Chrome
		$('.viewerMiddle').css('transform','scale(' + current + ')');
		//$('.viewerMiddle').css({'display':'inline'});
	  } else {
		step = 4;
		currIEZoom += step;
	      $('.viewerMiddle').css('zoom', ' ' + currIEZoom + '%');
	       }
	       zoomButtonEnable();
 });

  $('#scaleSelect').change(function(){
      var currFFZoom = $('#scaleSelect').val();
      $('.viewerMiddle').css('transform','scale(' + currFFZoom + ')');
      zoomButtonEnable();
  });
  $('#scaleSelect').click(function(){
      
  });
  function zoomButtonEnable(){
	  var zoomvalue = $('#scaleSelect').val();
	  if(zoomvalue<.4)
		$('#zoomOut').attr('disabled','disabled');
	    else if(zoomvalue>9)
		  $('#zoomIn').attr('disabled','disabled');
	    else{
	      $("#zoomOut").removeAttr('disabled');
	      $("#zoomIn").removeAttr('disabled');
	    }
	  $('#customScaleOption').hide();
	}
 
 
 $('#zoomOut').on('click',function(){
	    previous=$('#scaleSelect').val()*100;
	    for(i=1;i<=10;i++){
		  step=(i*100);
		    if(previous<step){
			step=i*10;
			break;      
		    }
	      }
	      current=(previous-step)/100;
	      $('#customScaleOption').show();
	      $('#customScaleOption').empty(); 
	      $('#customScaleOption').val(current);      
	      $('#scaleSelect option[value="'+current+'"]').text(parseInt(previous-(step))+'%');
	      $('#scaleSelect').val(current);	  
    
	      if ((navigator.userAgent.indexOf('Chrome') != -1||navigator.userAgent.indexOf('Firefox') != -1) && (
		parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Chrome') + 8)) >= 4.6 || parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Firefox') + 8)) >= 4.6)){//Chrome
		  $('.viewerMiddle').css('transform','scale(' + current + ')');
		}else {
		  step = 2;
		  currIEZoom -= step;
		  $('.viewerMiddle').css('zoom', ' ' + currIEZoom + '%');
	      }	  
	      zoomButtonEnable();
	  });
   
 
};// window.onload = function() ends
function printFun(){
  window.print();
}


// function next()
// {
//       var pageIndex = (parseInt($("#pageNumber").val()));
//     // alert($(".pageNumber").val());
//       if(pageIndex < totalPages){
// 	pageIndex+=1;
//       }else
// 	$('#next').attr('disabled','disabled');
//      $("#pageNumber").val(pageIndex);
//      $("#previous").removeAttr('disabled');
// //     navigatePage();
//        
// }
//     
// function previous(){
//   var currentPageIndex = $("#pageNumber").val();
//   if (currentPageIndex >1) {
//     console.log(currentPageIndex);
//     currentPageIndex = currentPageIndex - 1;
//       $("#pageNumber").val(currentPageIndex);
//   } else
//       $('#previous').attr('disabled','disabled');
//   $("#next").removeAttr('disabled');
// }

      $('#download').click(function(e) {
	  
    $.fileDownload($(this).prop('href'), {
        preparingMessageHtml: "We are preparing your report, please wait...",
        failMessageHtml: "There was a problem generating your report, please try again."
    });
    return false; 
});
