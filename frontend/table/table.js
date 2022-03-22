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
var addButton = document.getElementById("addButton");
var deleteButton = document.getElementById("deleteButton");
var updateButton = document.getElementById("updateButton");
var tableData;
deleteButton.addEventListener("click", deleteRow);
addButton.addEventListener("click", addRow);
updateButton.addEventListener("click", updateRow);
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
        var tableNames, TablePostParams, res;
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
                    // fetches the table data for the loaded sql database and table specified by dropdown from backend
                    tableData = _a.sent();
                    // parses data to table 
                    updateTable(tableData);
                    return [2 /*return*/];
            }
        });
    });
}
// updates the table specified by the dropdown menu when load is clicked
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
    var rowID = headerRow.insertCell(0);
    rowID.innerHTML = "row";
    for (var i = 0; i < headers.length; i++) {
        var currCell = headerRow.insertCell(i + 1);
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
        var rowID = currRow.insertCell(0);
        var rowCellText = document.createTextNode(String(i));
        rowID.appendChild(rowCellText);
        for (var _i = 0, _a = Array.from(headerMap.entries()); _i < _a.length; _i++) {
            var entry = _a[_i];
            var key = entry[0];
            var value = entry[1];
            var currCell = currRow.insertCell(key + 1);
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
function deleteRow() {
    return __awaiter(this, void 0, void 0, function () {
        var deleteInput, tableNames, deleteParams, res, updatedTableData;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    deleteInput = document.getElementById("delete_row");
                    tableNames = document.getElementById("tableNames");
                    deleteParams = {
                        name: tableNames.value,
                        row: tableData.rows[parseInt(deleteInput.value)]
                    };
                    return [4 /*yield*/, fetch("http://localhost:4567/delete", {
                            method: 'post',
                            body: JSON.stringify(deleteParams),
                            headers: {
                                'Content-Type': 'application/json; charset=UTF-8',
                                "Access-Control-Allow-Origin": "*"
                            }
                        })];
                case 1:
                    res = _a.sent();
                    return [4 /*yield*/, res.json()];
                case 2:
                    updatedTableData = _a.sent();
                    // parses data to table
                    updateTable(updatedTableData);
                    tableData = updatedTableData;
                    return [2 /*return*/];
            }
        });
    });
}
function addRow() {
    return __awaiter(this, void 0, void 0, function () {
        var addColumns, addValues, tableNames, addParams, res, updatedTableData;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    addColumns = document.getElementById("add_columns");
                    addValues = document.getElementById("add_values");
                    tableNames = document.getElementById("tableNames");
                    addParams = {
                        name: tableNames.value,
                        columns: addColumns.value,
                        values: addValues.value
                    };
                    return [4 /*yield*/, fetch("http://localhost:4567/add", {
                            method: 'post',
                            body: JSON.stringify(addParams),
                            headers: {
                                'Content-Type': 'application/json; charset=UTF-8',
                                "Access-Control-Allow-Origin": "*"
                            }
                        })];
                case 1:
                    res = _a.sent();
                    return [4 /*yield*/, res.json()];
                case 2:
                    updatedTableData = _a.sent();
                    // parses data to table
                    updateTable(updatedTableData);
                    tableData = updatedTableData;
                    return [2 /*return*/];
            }
        });
    });
}
function updateRow() {
    return __awaiter(this, void 0, void 0, function () {
        var updateRow, updateColumns, newValues, tableNames, updateParams, res, updatedTableData;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    updateRow = document.getElementById("update_row");
                    updateColumns = document.getElementById("update_column");
                    newValues = document.getElementById("new_value");
                    tableNames = document.getElementById("tableNames");
                    updateParams = {
                        name: tableNames.value,
                        row: tableData.rows[parseInt(updateRow.value)],
                        columns: updateColumns.value,
                        values: newValues.value
                    };
                    return [4 /*yield*/, fetch("http://localhost:4567/update", {
                            method: 'post',
                            body: JSON.stringify(updateParams),
                            headers: {
                                'Content-Type': 'application/json; charset=UTF-8',
                                "Access-Control-Allow-Origin": "*"
                            }
                        })];
                case 1:
                    res = _a.sent();
                    return [4 /*yield*/, res.json()];
                case 2:
                    updatedTableData = _a.sent();
                    // parses data to table
                    updateTable(updatedTableData);
                    tableData = updatedTableData;
                    return [2 /*return*/];
            }
        });
    });
}
