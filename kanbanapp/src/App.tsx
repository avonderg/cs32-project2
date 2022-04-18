import React, {useState} from 'react';
import Table from './components/Table/Table';
import "./styles.css"

function App() {

  type table = {
    headers: string[];
    rows: Record<string, string>[];
    // cols: Record<string,string>[];
  }

  type selectedTable = {
    currentTable: string;
  }

  type ApiRequest = {
    command: string;
  }

  const [currTableData, setCurrTableData] = useState<table>({headers: ["empty"], rows:[{"empty": "empty"}]})

  const requestKanban = async() => {
    fetch("http://localhost:4567/loadKanban", {
      method: "GET",
      headers: {
        "Access-Control-Allow-Origin": "*"
      },
    }).then((res: Response) => res.json()).then((tableData: table) => setCurrTableData(tableData));
  }

  const displayKanban = async() => {
    requestKanban()
  }
  const updateKanban = async function(input: string) {
    console.log("updating")
    console.log(input)
    var filter:string, table, tr, td, cell;
    filter = input.toUpperCase()
    table = document.getElementById("kanbantable")!;
    console.log(table)
    tr = table.getElementsByTagName("tr");
    for (var i = 1; i < tr.length; i++) {
      // Hide the row initially.
      tr[i].style.display = "none";

      td = tr[i].getElementsByTagName("td");
      for (var j = 0; j < td.length; j++) {
        cell = tr[i].getElementsByTagName("td")[j];
        if (cell) {
          if (cell.innerHTML.toUpperCase().indexOf(filter) > -1) {
            tr[i].style.display = "";
            break;
          }
        }
      }
    }
  }

  return (
      <div className="App">
        <div className = "header">
          <header className="App-header">
            <h1> Sprint 4 Kanban Loader </h1>
          </header>

        </div>

        <div className = 'button-container'>
          <button className = "loader" id = "loader" onClick={displayKanban}>
            <span className = "btn-text">Load Kanban</span>
          </button>

          <input type = "text" id = "myInput" onKeyUp = {(e: React.KeyboardEvent<HTMLInputElement>)=> {
            updateKanban((e.target as HTMLInputElement).value)
          }} placeholder="Search Table"></input>
        </div>

        <div className = "kanbandiv" id = "kanbantable">
          <Table tableInfo = {currTableData} setRowMap = {setCurrTableData}/>
        </div>

      </div>


  );
}

export default App;
