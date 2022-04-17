import React, {useState} from 'react';
import Table from './components/Table/Table';
import "./styles.css"

function App() {

  type table = {
    headerList: string[];
    rowMap: Record<string, string>[];
  }

  type selectedTable = {
    currentTable: string;
  }

  type ApiRequest = {
    command: string;
  }

  const [currTableData, setCurrTableData] = useState<table>({headerList: ["empty"], rowMap:[{"empty": "empty"}]})

  const requestKanban = async() => {
    fetch("http://localhost:4567/loadkanban", {
      method: "GET",
      headers: {
        "Access-Control-Allow-Origin": "*"
      },
    }).then((res: Response) => res.json()).then((tableData: table) => setCurrTableData(tableData));

  }

  const displayKanban = async() => {

    requestKanban()
    console.log(currTableData)
  }


  return (
      <div className="App">
        <div className = "header">
          <header className="App-header">
            <h1> Sprint 4 Kanban Loader </h1>
          </header>

        </div>

        <div>
          <button className = "loadButton" id = "loadButton" onClick={displayKanban}>
            <span className = "btn-text">Load Kanban</span>
          </button>
        </div>

        <div className = "kanbantable">
          <Table tableInfo = {currTableData} setRowMap = {setCurrTableData}/>
        </div>

      </div>


  );
}

export default App;
