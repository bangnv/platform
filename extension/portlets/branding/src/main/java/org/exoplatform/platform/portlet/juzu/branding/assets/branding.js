$(function() {
	var fileUpload;
	$("#save").on("click", function() {
		if (validate(fileUpload)){
			$('#form').submit();
		}
		var valueSelected = ($('.target option:selected').val());
		$('#result').jzLoad("BrandingControler.saveParameter()", {
			"style" : valueSelected
		});
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

	$('.target').change(function() {
		//		alert ($('.target option:selected').val());
		var valueSelected = ($('.target option:selected').val());
	});
	
	
	function preview(input) {
		fileUpload=input;
		var checkValide  = validate(input);
		if (checkValide==false) {
			$("div#result").text("the file must be in photo format gif | png | bmp | jpeg |jpg " );
			return;
		}
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#preview_image').attr('src', e.target.result).width(300).height(
					80);
		};
		reader.readAsDataURL(input.files[0]);
		$("div#result").text("");
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


