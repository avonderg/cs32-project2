// run tsc in this directory to compile to js before testing
type Table = {
    name: string,
    headers: string[],
    rows: Record<string, string>[]
}

type ServerError = {
    error: string
}

type TableName = {
    name: string,
}

const loadButton : HTMLButtonElement = document.getElementById("loader") as HTMLButtonElement;
const dropdown : HTMLDivElement = document.getElementById("dropdown") as HTMLDivElement;
const table : HTMLTableElement = document.getElementById("displayTable") as HTMLTableElement;
let rowCount : number = 0;

fetch("http://localhost:4567/tableNames").then((res : Response) => res.json()).then((tableNames: string[]) => updateDropdown(tableNames))

// updates dropdown menu as soon as table names are loaded from backend
function updateDropdown(tableNames : string[]) : void { 
    dropdown.innerHTML = "";
    dropdown.innerHTML += "<select name=\"tables\" id=\"tableNames\"></select>"; 
    
    let tableNamesElement : HTMLSelectElement = document.getElementById("tableNames") as HTMLSelectElement; 
    tableNames.forEach((table : string) => tableNamesElement.innerHTML += `<option value=\"${table}\">${table}</option>`);
    // added all the table names to the drop down menu
}

loadButton.addEventListener("click", load)

// on load click, gets the table specified by dropdown menu from backend and then updates table html
async function load() : Promise<void> {
    let tableNames : HTMLSelectElement = document.getElementById("tableNames") as HTMLSelectElement;

    const TablePostParams : TableName = {
        name: tableNames.value,
    };
    
    const res : Response = await fetch("http://localhost:4567/table", {
    method: 'post',
    body: JSON.stringify(TablePostParams),
    headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        "Access-Control-Allow-Origin":"*"
        }
    });
    // fetches the table data for the loaded sql database and table specified by dropdown from backend

    let tableData : Table = await res.json();
    // parses data to table 

    updateTable(tableData);
}

// updates the table specified by thte dropdown menu when load is clicked
function updateTable(tableData : Table) : void{
    table.innerHTML = "";
    let headerRow : HTMLTableRowElement = table.insertRow()

    let headerMap : Map<number, string>  = insertHeaders(tableData.headers, headerRow)
    // returns map from column index to header so that can insert row data at appropriate positions

    console.log(headerMap)
    console.log(tableData.rows)
    insertRows(table, tableData.rows, headerMap)
}

// generates the html to insert the header row into the table
function insertHeaders(headers : string[], headerRow : HTMLTableRowElement) : Map<number, string> {
    let map : Map<number, string>  = new Map<number, string>();
    
    for (let i = 0; i < headers.length; i++) {
        let currCell : HTMLTableCellElement = headerRow.insertCell(i)
        currCell.innerHTML = headers[i]; 
        map.set(i, headers[i])
    }

    return map;
    // returns map from column index to header 
}

// generates the html to insert rows into the table
function insertRows(table : HTMLTableElement, rows :  Record<string, string>[], headerMap :  Map<number, string>) {
    for (let i = 0; i < rows.length; i++) { 
        let currRow : HTMLTableRowElement = table.insertRow()
        for (let entry of Array.from(headerMap.entries())) { 
            let key = entry[0];
            let value = entry[1];

            let currCell : HTMLTableCellElement = currRow.insertCell(key)
            let currRecord : Record<string, string> = rows[i]
            // gets the value for the specified column header (in this row)

            console.log(currRecord)
            console.log(key)
            console.log(currRecord[value])

            let cellText = document.createTextNode(currRecord[value]);
            currCell.appendChild(cellText)
            // puts the value intot the cell
        } 
    }
}