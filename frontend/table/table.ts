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

function updateDropdown(tableNames : string[]) : void { 
    dropdown.innerHTML = "";
    dropdown.innerHTML += "<select name=\"tables\" id=\"tableNames\"></select>"; 
    
    let tableNamesElement : HTMLSelectElement = document.getElementById("tableNames") as HTMLSelectElement; 
    tableNames.forEach((table : string) => tableNamesElement.innerHTML += `<option value=\"${table}\">${table}</option>`);
    console.log(tableNames);

}

loadButton.addEventListener("click", load)

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

    let tableData : Table = await res.json();
    console.log(tableData);

    updateTable(tableData);
}

function updateTable(tableData : Table) : void{
    table.innerHTML = "";
    let headerRow : HTMLTableRowElement = table.insertRow()

    let headerMap : Map<number, string>  = insertHeaders(tableData.headers, headerRow)
    console.log(headerMap)
    console.log(tableData.rows)
    insertRows(table, tableData.rows, headerMap)
    console.log("REACHED HERE")
}

function insertHeaders(headers : string[], headerRow : HTMLTableRowElement) : Map<number, string> {
    let map : Map<number, string>  = new Map<number, string>();
    
    for (let i = 0; i < headers.length; i++) {
        let currCell : HTMLTableCellElement = headerRow.insertCell(i)
        currCell.innerHTML = headers[i]; 
        map.set(i, headers[i])
    }

    return map;
}

function insertRows(table : HTMLTableElement, rows :  Record<string, string>[], headerMap :  Map<number, string>) {

    for (let i = 0; i < rows.length; i++) { 
        let currRow : HTMLTableRowElement = table.insertRow()
        for (let entry of Array.from(headerMap.entries())) { 
            let key = entry[0];
            let value = entry[1];

            let currCell : HTMLTableCellElement = currRow.insertCell(key)
            let currRecord : Record<string, string> = rows[i]

            console.log(currRecord)
            console.log(key)
            console.log(currRecord[value])

            let cellText = document.createTextNode(currRecord[value]);
            currCell.appendChild(cellText)
        } 
    }
}