function bufAction(s) {
	if (typeof ArrayBuffer !== 'undefined') {
		var buf = new ArrayBuffer(s.length);
		var view = new Uint8Array(buf);
		for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
		return buf;
	} else {
		var buf = new Array(s.length);
		for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xFF;
		return buf;
	}
}

function chaneAction() {
	var file = $('#excelInput').get(0).files[0];;
	var reader = new FileReader();
	reader.onload = function(e) {
		var binary = "";
		var bytes = new Uint8Array(e.target.result);
		var length = bytes.byteLength;
		for (var i = 0; i < length; i++) {
			binary += String.fromCharCode(bytes[i]);
		}
		var wb = XLSX.read(binary, {
			type: 'binary'
		});
		var wsname = wb.SheetNames[0];
		var ws = wb.Sheets[wsname];
		var HTML = XLSX.utils.sheet_to_html(ws);
		document.getElementById('out-table').innerHTML = HTML;
		//table筛选等等可以在此操作
		document.getElementById('export-table').style.visibility = "visible";
	};
	reader.readAsArrayBuffer(file);
}

function exportAction() {
	var wb = XLSX.utils.table_to_book(document.getElementById('out-table'));
	var wbout = XLSX.write(wb, {
		bookType: 'xlsx',
		type: 'binary'
	});
	saveAs(new Blob([bufAction(wbout)], {
		type: 'application/octet-stream'
	}), "导出列表.xlsx");
}