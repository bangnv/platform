$(function() {

	var fileUpload;
	getAndUpdateStyleNavigationByAjax();

	$("#cancel").on(
			"click",
			function() {
				getAndUpdateStyleNavigationByAjax();
				getAndUpdateLogoByAjax();
				$("div#result").text(
						"Changes in branding settings have been cancelled");
			});
	$("#save").on("click", function() {
		var valueSelected = ($('#navigationStyle option:selected').val());
		$("#style").val(valueSelected);
		$('#form').submit();
		// $('#result').jzLoad("BrandingControler.saveParameter()", {
		// "style" : valueSelected
		// });
	});

	$("input#file").on("change", function() {
		preview(this);
	});

	$('#form').submit(function() {
		$(this).ajaxSubmit({
			target : '#result'
		});
		return false;
	});

	function preview(input) {
		fileUpload = input;
		var checkValide = validate(input);
		if (checkValide == false) {
			// not validated
			$("div#result").text("the file must be in photo format png ");
			$("#file").replaceWith($("#file").val("").clone(true));
			return;
		} else {
			// validated
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#preview_image').attr('src', e.target.result).width(300)
						.height(80);
				$('#preview1').attr('src', e.target.result);
				$('#preview2').attr('src', e.target.result);
			};
			reader.readAsDataURL(input.files[0]);

			$("div#result").text("");
		}
	}

	function validate(input) {
		var valide = false;
		if (input != null && input.files && input.files[0]) {
			var fileName = input.value;
			var extension = fileName.substring(fileName.lastIndexOf('.') + 1)
					.toLowerCase();
			if (extension == "png") {
				valide = true;
			}
		}
		return valide;
	}

	function getAndUpdateLogoByAjax() {
		$("#preview_image").jzAjax({
			url : "BrandingControler.getLogoUrlByAjax()",
			success : function(data) {
				$('#preview_image').attr('src', data).width(300).height(80);
				$('#preview1').attr('src', data);
				$('#preview2').attr('src', data);
			}
		});
	}

	function getAndUpdateStyleNavigationByAjax() {
		$("#navigationStyle").jzAjax({
			url : "BrandingControler.getStyleValue()",
			success : function(data) {
				$("#navigationStyle").val(data).attr('selected', 'selected');
			}
		});
	}

});
