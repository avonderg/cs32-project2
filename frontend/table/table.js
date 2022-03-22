var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var loadButton = document.getElementById("loader");
var dropdown = document.getElementById("dropdown");
var table = document.getElementById("displayTable");
var rowCount = 0;
fetch("http://localhost:4567/tableNames").then(function (res) { return res.json(); }).then(function (tableNames) { return updateDropdown(tableNames); });
// updates dropdown menu as soon as table names are loaded from backend
function updateDropdown(tableNames) {
    dropdown.innerHTML = "";
    dropdown.innerHTML += "<select name=\"tables\" id=\"tableNames\"></select>";
    var tableNamesElement = document.getElementById("tableNames");
    tableNames.forEach(function (table) { return tableNamesElement.innerHTML += "<option value=\"".concat(table, "\">").concat(table, "</option>"); });
    // added all the table names to the drop down menu
}
loadButton.addEventListener("click", load);
// on load click, gets the table specified by dropdown menu from backend and then updates table html
function load() {
    return __awaiter(this, void 0, void 0, function () {
        var tableNames, TablePostParams, res, tableData;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    tableNames = document.getElementById("tableNames");
                    TablePostParams = {
                        name: tableNames.value
                    };
                    return [4 /*yield*/, fetch("http://localhost:4567/table", {
                            method: 'post',
                            body: JSON.stringify(TablePostParams),
                            headers: {
                                'Content-Type': 'application/json; charset=UTF-8',
                                "Access-Control-Allow-Origin": "*"
                            }
                        })];
                case 1:
                    res = _a.sent();
                    return [4 /*yield*/, res.json()];
                case 2:
                    tableData = _a.sent();
                    // parses data to table 
                    updateTable(tableData);
                    return [2 /*return*/];
            }
        });
    });
}
// updates the table specified by thte dropdown menu when load is clicked
function updateTable(tableData) {
    table.innerHTML = "";
    var headerRow = table.insertRow();
    var headerMap = insertHeaders(tableData.headers, headerRow);
    // returns map from column index to header so that can insert row data at appropriate positions
    console.log(headerMap);
    console.log(tableData.rows);
    insertRows(table, tableData.rows, headerMap);
}
// generates the html to insert the header row into the table
function insertHeaders(headers, headerRow) {
    var map = new Map();
    for (var i = 0; i < headers.length; i++) {
        var currCell = headerRow.insertCell(i);
        currCell.innerHTML = headers[i];
        map.set(i, headers[i]);
    }
    return map;
    // returns map from column index to header 
}
// generates the html to insert rows into the table
function insertRows(table, rows, headerMap) {
    for (var i = 0; i < rows.length; i++) {
        var currRow = table.insertRow();
        for (var _i = 0, _a = Array.from(headerMap.entries()); _i < _a.length; _i++) {
            var entry = _a[_i];
            var key = entry[0];
            var value = entry[1];
            var currCell = currRow.insertCell(key);
            var currRecord = rows[i];
            // gets the value for the specified column header (in this row)
            console.log(currRecord);
            console.log(key);
            console.log(currRecord[value]);
            var cellText = document.createTextNode(currRecord[value]);
            currCell.appendChild(cellText);
            // puts the value intot the cell
        }
    }
}
