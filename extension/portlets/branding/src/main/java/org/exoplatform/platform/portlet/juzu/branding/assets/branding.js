$(function() {
	var fileUpload;
	UpdatePreviewLogoAndStyle(true);
	// clone Toolbar Administrator and add to Preview
	$("#PlatformAdminToolbarContainer").clone().appendTo($("#StylePreview"));
	fixSearchInput();
	scaleToFitPreviewImg($('#PreviewImg'));
//	runDragandDrop();
	
	$("#cancel").on(
			"click",
			function() {
				$("#ajaxUploading").show();
				UpdatePreviewLogoAndStyle(false);
				$("div#result").text(
						"Changes in branding settings have been cancelled");
			});
	$("#save").on("click", function() {
		$("#style").val(($('#navigationStyle option:selected').val()));
		var result = $('#form').submit();
		return result;
	});

	$("input#file").on("change", function() {
		previewLogoBycontent(this.files[0]);
	});

	
	$('#form').submit(function() {
		$(this).ajaxSubmit({
			beforeSubmit : function(data) {
				if (validate($("input#file"))){
				$("#ajaxUploading").show();
				}
			},
			target : '#result',
			success : function(data) {
				UpdateTopBarNavigation(data);
				$("#ajaxUploading").hide();
				$("div#result").text(
				"Changes in branding settings have been saved");
			}
		});

		return false;
	});

//	$('.target').change(function() {
//		var valueSelected = ($('.target option:selected').val());
//	});

	/* Change CSS by selecting */
	$("#navigationStyle").change(function() {
		var style = $("#navigationStyle").val();
		changePreviewStyle(style);

	});

	function changePreviewStyle(style) {
		var idContainer = $("#StylePreview #UIToolbarContainer");
		idContainer.removeAttr("class");
		idContainer.addClass("UIToolbarContainer" + style + " UIContainer");
	}

	function fixSearchInput() {
		$(
				"#StylePreview #UIToolbarContainer #SearchNavigationTabsContainer input")
				.remove();
	}
	function previewLogoBycontent(file) {
		var checkValide = validate(file);
		if (checkValide == false) {
			// not validated
			$("div#result").text("the file must be in photo format png ");
			return;
		} else {
			// validated
			var reader = new FileReader();
			reader.onload = function(e) {
				previewPhoto(e.target.result);
			};
			reader.readAsDataURL(file);
			$("div#result").text("");
		}
	}
	
		function validate(inputForm){
			return validate(inputForm.files[0]);
		}

		function validate(file) {
		if (file.type=="image/png") {
			return true;
		} else {
			$("#file").replaceWith($("#file").val("").clone(true));
			return false;
		}
	}

	function scaleToFitPreviewImg(elt) {
		$(elt).imgscale({
			parent : '.non-immediate-parent-container',
			fade : 1000
		});
	}

	function previewPhoto(data) {
		$('#PreviewImg').attr('src', data);
		scaleToFitPreviewImg($('#PreviewImg'));
		$('#StylePreview #HomeLink img').attr('src', data).width(25).height(21);
	}

	function UpdatePreviewLogoAndStyle(firstTime) {
		$("#navigationStyle").jzAjax({
			
			url : "BrandingControler.getResource()",
			beforeSend: function(){
//				alert("beforesend");
				if(!firstTime) {
					$("#ajaxUploading").show();
				}
			},
			success : function(data) {
				// update the logo url in preview zone and preview navigation bar
				previewPhoto(data.logoUrl);
				// update the navigation style and style selected;
				changePreviewStyle(data.style);
				$("#navigationStyle").val(data.style).attr('selected', 'selected');
				$("#ajaxUploading").hide();
			}
		});
	}
	function UpdateTopBarNavigation(data) {
		$("#PlatformAdminToolbarContainer .HomeLink img:first").attr('src',
				data.logoUrl).width(25).height(21);
		$("#PlatformAdminToolbarContainer #UIToolbarContainer:first")
				.removeAttr("class");
		$("#PlatformAdminToolbarContainer #UIToolbarContainer:first").addClass(
				"UIToolbarContainer" + data.style + " UIContainer");
	}
	
	function $id(id) {
		return document.getElementById(id);
	}
	function FileDragHover(e) {
		e.stopPropagation();
		e.preventDefault();
		e.target.className = (e.type == "dragover" ? "hover" : "");
	}
	
	// file selection
	function FileSelectHandler(e) {
		// cancel event and hover styling
		FileDragHover(e);
		var files = e.target.files || e.dataTransfer.files;
		previewLogoBycontent(files[0]);
		// fetch FileList object
		// input = e.target
		
		
		
//		var files = e.target.files || e.dataTransfer.files;
//		// process all File objects
//		for (var i = 0, f; f = files[i]; i++) {
//			ParseFile(f);
//		}
	}
	
//	function ParseFile(file) {
//		Output(
//			"<p>File information: <strong>" + file.name +
//			"</strong> type: <strong>" + file.type +
//			"</strong> size: <strong>" + file.size +
//			"</strong> bytes</p>"
//		);
//	}
	
	// call initialization file
	if (window.File && window.FileList && window.FileReader) {
		Init();
	}
	// initialize
	function Init() {
		var filedrag = $id("filedrag");
//		fileselect.addEventListener("change", FileSelectHandler, false);
		// is XHR2 available?
		var xhr = new XMLHttpRequest();
		if (xhr.upload) {
			// file drop
			filedrag.addEventListener("dragover", FileDragHover, false);
			filedrag.addEventListener("dragleave", FileDragHover, false);
			filedrag.addEventListener("drop", FileSelectHandler, false);
			filedrag.style.display = "block";
		}
	}
	
	


	
});
