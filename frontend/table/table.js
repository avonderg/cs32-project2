"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
const loadButton = document.getElementById("loader");
const dropdown = document.getElementById("dropdown");
const table = document.getElementById("displayTable");
let rowCount = 0;
const addButton = document.getElementById("addButton");
const deleteButton = document.getElementById("deleteButton");
const updateButton = document.getElementById("updateButton");
let tableData;
deleteButton.addEventListener("click", deleteRow);
addButton.addEventListener("click", addRow);
updateButton.addEventListener("click", updateRow);
fetch("http://localhost:4567/tableNames").then((res) => res.json()).then((tableNames) => updateDropdown(tableNames));
// updates dropdown menu as soon as table names are loaded from backend
function updateDropdown(tableNames) {
    dropdown.innerHTML = "";
    dropdown.innerHTML += "<select name=\"tables\" id=\"tableNames\"></select>";
    let tableNamesElement = document.getElementById("tableNames");
    tableNames.forEach((table) => tableNamesElement.innerHTML += `<option value=\"${table}\">${table}</option>`);
    // added all the table names to the drop down menu
}
loadButton.addEventListener("click", load);
// on load click, gets the table specified by dropdown menu from backend and then updates table html
function load() {
    return __awaiter(this, void 0, void 0, function* () {
        let tableNames = document.getElementById("tableNames");
        const TablePostParams = {
            name: tableNames.value,
        };
        const res = yield fetch("http://localhost:4567/table", {
            method: 'post',
            body: JSON.stringify(TablePostParams),
            headers: {
                'Content-Type': 'application/json; charset=UTF-8',
                "Access-Control-Allow-Origin": "*"
            }
        });
        // fetches the table data for the loaded sql database and table specified by dropdown from backend
        tableData = yield res.json();
        // parses data to table 
        updateTable(tableData);
    });
}
// updates the table specified by the dropdown menu when load is clicked
function updateTable(tableData) {
    table.innerHTML = "";
    let headerRow = table.insertRow();
    let headerMap = insertHeaders(tableData.headers, headerRow);
    // returns map from column index to header so that can insert row data at appropriate positions
    console.log(headerMap);
    console.log(tableData.rows);
    insertRows(table, tableData.rows, headerMap);
}
// generates the html to insert the header row into the table
function insertHeaders(headers, headerRow) {
    let map = new Map();
    let rowID = document.createElement("th"); //headerRow.insertCell(0)
    rowID.innerHTML = "row";
    headerRow.appendChild(rowID);
    for (let i = 0; i < headers.length; i++) {
        let currCell = document.createElement("th"); //headerRow.insertCell(i + 1)
        currCell.innerHTML = headers[i];
        headerRow.appendChild(currCell);
        map.set(i, headers[i]);
    }
    return map;
    // returns map from column index to header 
}
// generates the html to insert rows into the table
function insertRows(table, rows, headerMap) {
    for (let i = 0; i < rows.length; i++) {
        let currRow = table.insertRow();
        let rowID = currRow.insertCell(0);
        let rowCellText = document.createTextNode(String(i));
        rowID.appendChild(rowCellText);
        for (let entry of Array.from(headerMap.entries())) {
            let key = entry[0];
            let value = entry[1];
            let currCell = currRow.insertCell(key + 1);
            let currRecord = rows[i];
            // gets the value for the specified column header (in this row)
            console.log(currRecord);
            console.log(key);
            console.log(currRecord[value]);
            let cellText = document.createTextNode(currRecord[value]);
            currCell.appendChild(cellText);
            // puts the value intot the cell
        }
    }
}
function deleteRow() {
    return __awaiter(this, void 0, void 0, function* () {
        let deleteInput = document.getElementById("delete_row");
        let tableNames = document.getElementById("tableNames");
        const deleteParams = {
            name: tableNames.value,
            row: tableData.rows[parseInt(deleteInput.value)]
        };
        const res = yield fetch("http://localhost:4567/delete", {
            method: 'post',
            body: JSON.stringify(deleteParams),
            headers: {
                'Content-Type': 'application/json; charset=UTF-8',
                "Access-Control-Allow-Origin": "*"
            }
        });
        // fetches the table data for the loaded sql database and table specified by dropdown from backend
        let updatedTableData = yield res.json();
        // parses data to table
        updateTable(updatedTableData);
        tableData = updatedTableData;
    });
}
function addRow() {
    return __awaiter(this, void 0, void 0, function* () {
        let addColumns = document.getElementById("add_columns");
        let addValues = document.getElementById("add_values");
        let tableNames = document.getElementById("tableNames");
        const addParams = {
            name: tableNames.value,
            columns: addColumns.value,
            values: addValues.value
        };
        const res = yield fetch("http://localhost:4567/add", {
            method: 'post',
            body: JSON.stringify(addParams),
            headers: {
                'Content-Type': 'application/json; charset=UTF-8',
                "Access-Control-Allow-Origin": "*"
            }
        });
        // fetches the table data for the loaded sql database and table specified by dropdown from backend
        let updatedTableData = yield res.json();
        // parses data to table
        updateTable(updatedTableData);
        tableData = updatedTableData;
    });
}
function updateRow() {
    return __awaiter(this, void 0, void 0, function* () {
        let updateRow = document.getElementById("update_row");
        let updateColumns = document.getElementById("update_column");
        let newValues = document.getElementById("new_value");
        let tableNames = document.getElementById("tableNames");
        const updateParams = {
            name: tableNames.value,
            row: tableData.rows[parseInt(updateRow.value)],
            columns: updateColumns.value,
            values: newValues.value
        };
        const res = yield fetch("http://localhost:4567/update", {
            method: 'post',
            body: JSON.stringify(updateParams),
            headers: {
                'Content-Type': 'application/json; charset=UTF-8',
                "Access-Control-Allow-Origin": "*"
            }
        });
        // fetches the table data for the loaded sql database and table specified by dropdown from backend
        let updatedTableData = yield res.json();
        // parses data to table
        updateTable(updatedTableData);
        tableData = updatedTableData;
    });
}
