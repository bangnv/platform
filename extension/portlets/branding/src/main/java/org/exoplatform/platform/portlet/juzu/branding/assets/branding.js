$(function() {

//	  var dropbox = $('#dropbox'),
//		message = $('.message', dropbox);

//  dropbox.filedrop({
//      url: 'http://localhost:62615/Home/UploadFiles',
//      paramname: 'files',
//      maxFiles: 10,
//      maxFileSize: 4,
//      allowedfiletypes: ['image/jpeg', 'image/png', 'image/gif'],
//      uploadFinished: function (i, file, response) {
//          $.data(file).addClass('done');
//          // response is the JSON object that controller returns
//      },
//
//      error: function (err, file) {
//          switch (err) {
//              case 'BrowserNotSupported':
//                  showMessage('Your browser does not support HTML5 file uploads!');
//                  break;
//              case 'TooManyFiles':
//                  alert('Too many files! Please select 10 at most! (configurable)');
//                  break;
//              case 'FileTooLarge':
//                  alert(file.name + ' is too large! Please upload files up to 4mb (configurable).');
//                  break;
//              case 'FileTypeNotAllowed':
//                  alert(file.name + ' is not supported. You can only upload files with .gif .png and .jpg extension');
//                  break;
//              default:
//                  break;
//          }
//      },
//
//      uploadStarted: function (i, file, len) {
//          createImage(file);
//      },
//
//      progressUpdated: function (i, file, progress) {
//          $.data(file).find('.progress').width(progress);
//      }
//
//  });

//  var template = '<div class="preview">' +
//			'<span class="imageHolder">' +
//			    '<img />' +
//			    '<span class="uploaded"></span>' +
//			'</span>' +
//			'<div class="progressHolder">' +
//				'<div class="progress"></div>' +
//			'</div>' +
//                    '<div class="remove">Remove</div>' +
//		     '</div>';

//  function createImage(file) {
//      var preview = $(template), image = $('img', preview);
//
//      var reader = new FileReader();
//
//      image.width = 100;
//      image.height = 100;
//
//      reader.onload = function (e) {
//
//          // e.target.result holds the DataURL which
//          // can be used as a source of the image:
//
//          image.attr('src', e.target.result);
//      };
//
//      // Reading the file as a DataURL. When finished,
//      // this will trigger the onload function above:
//      reader.readAsDataURL(file);
//
//      message.hide();
//      preview.appendTo(dropbox);
//
//      // Associating a preview container
//      // with the file, using jQuery's $.data():
//
//      $.data(file, preview);
//  }

//  function showMessage(msg) {
//      message.html(msg);
//  }
	
	
	
	
	var fileUpload;

	      $("#navigationStyle").jzAjax({url:"BrandingControler.getStyleValue()",
	    		   success:function(data) {
	    				 $("#navigationStyle").val(data).attr('selected', 'selected');
	    		   }}
	      );    
	
	$("#save").on("click", function() {
		var valueSelected = ($('#navigationStyle option:selected').val());
		$("#style").val(valueSelected);
//		if (validate(fileUpload)){
//		}
		$('#form').submit();
//		$('#result').jzLoad("BrandingControler.saveParameter()", {
//			"style" : valueSelected
//		});
	});
	
	$("input#file").on("change",function(){
		preview(this);
	});

	$('#form').submit(function() {
		$(this).ajaxSubmit({
			target : '#result'
		});
		return false;
	});

//	$('.target').change(function() {
//		//		alert ($('.target option:selected').val());
//		var valueSelected = ($('.target option:selected').val());
//	});
	
	
	function preview(input) {
		fileUpload=input;
		var checkValide  = validate(input);
		if (checkValide==false) {
		// not validated
			$("div#result").text("the file must be in photo format gif | png | bmp | jpeg |jpg " );
			$("#file").replaceWith($("#file").val("").clone(true));
			return;
		} 
		else {
		// validated
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#preview_image').attr('src', e.target.result).width(300).height(
					80);
			$('#preview1').attr('src', e.target.result);
			$('#preview2').attr('src', e.target.result);
		};
		reader.readAsDataURL(input.files[0]);
		
		$("div#result").text("");
		}
}

function validate(input) {
	var valide=false;
	if (input!=null && input.files && input.files[0]) {
		var fileName=input.value;
		var extension = fileName.substring(
				fileName.lastIndexOf('.') + 1).toLowerCase();
		if (extension == "gif" || extension == "png" || extension == "bmp"
				|| extension == "jpeg" || extension == "jpg") {
			valide = true; 
		} 
	}
	return valide;
}


function triggerSave() {
	alert("hi");
	var valueSelected = ($('.target option:selected').val());
	$('#result').jzLoad("BrandingControler.saveParameter()", {
		"style" : valueSelected
	});
}
});





//$(function() {
//
//	function preview(input) {
//
//		if (input.files && input.files[0]) {
//			var reader = new FileReader();
//
//			reader.onload = function(e) {
//				$('#preview_image').attr('src', e.target.result).width(100)
//						.height(120);
//			};
//
//			reader.readAsDataURL(input.files[0]);
//		}
//	}
//	;
//});


