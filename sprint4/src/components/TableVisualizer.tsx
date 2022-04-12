import React, { useEffect } from "react";
import { useState } from "react";
import axios from "axios";
import Table from "./Table";
import TableLoader from "./TableLoader";
// @ts-ignore
import { AwesomeButton } from "react-awesome-button";
// @ts-ignore
import "react-awesome-button/dist/styles.css";

function TableVisualizer() {
  const [tableName, setTableName] = useState("");
  const [tableRows, setTableRows] = useState([]);
  const [tableNames, setTableNames] = useState([]);
  const [tableHeaders, setTableHeaders] = useState([]);
  const [addInputs, setAddInputs] = useState(new Map());
  const [updateInputs, setUpdateInputs] = useState(new Map());
  const [rowToUpdate, setRowToUpdate] = useState("");

  type AddParams = {
    name: string;
    columns: string[];
    values: string[];
  };

  type DeleteParams = {
    name: string;
    row: Record<string, string>;
  };

  type UpdateParams = {
    name: string;
    row: Record<string, string>;
    columns: string[];
    values: string[];
  };

  let config = {
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
  };

  const getNames = () => {
    axios
      .get("http://localhost:4567/tableNames", config)
      .then((response) => {
        console.log(response.data);
        setTableNames(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const getTable = (sortCol: string) => {
    if (tableName == "") {
      return;
    }
    const toSend = {
      name: tableName,
      sortCol: sortCol,
    };
    axios
      .post("http://localhost:4567/table", toSend, config)
      .then((response) => {
        console.log(response.data);
        console.log(response.data["rows"]);
        console.log(response.data["headers"]);

        setTableRows(response.data["rows"]);
        setTableHeaders(response.data["headers"]);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const addRow = (toSend: AddParams) => {
    axios
      .post("http://localhost:4567/add", toSend, config)
      .then((response) => {
        console.log(response.data);
        setTableRows(response.data.rows);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const deleteRow = (rowToDelete: Record<string, string>) => {
    let toSend: DeleteParams = {
      name: tableName,
      row: rowToDelete,
    };
    axios
      .post("http://localhost:4567/delete", toSend, config)
      .then((response) => {
        console.log(response.data);
        setTableRows(response.data.rows);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const updateRow = (toSend: UpdateParams) => {
    axios
      .post("http://localhost:4567/update", toSend, config)
      .then((response) => {
        console.log(response.data);
        setTableRows(response.data.rows);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const loadAddInputBoxes = () => {
    let inputBoxes = [];
    for (let i = 0; i < tableHeaders.length; i++) {
      inputBoxes.push(
        <div className="inputField">
          <label htmlFor={tableHeaders[i]}>{tableHeaders[i]}:</label>
          <input
            className="clearInput"
            type="text"
            onChange={(e) => {
              console.log("changed input");
              setAddInputs(
                (map) => new Map(map.set(tableHeaders[i], e.target.value))
              );
            }}
          />
        </div>
      );
    }
    return inputBoxes;
  };

  const loadUpdateInputBoxes = () => {
    let inputBoxes = [];
    inputBoxes.push(
      <div className="inputField">
        <label htmlFor="rowNum">Row Number: </label>
        <input
          className="clearInputNum"
          type="number"
          min="1"
          max={tableRows.length}
          onChange={(e) => {
            setRowToUpdate(e.target.value);
          }}
        />
      </div>
    );
    for (let i = 0; i < tableHeaders.length; i++) {
      inputBoxes.push(
        <div className="inputField">
          <label htmlFor={tableHeaders[i]}>{tableHeaders[i]}:</label>
          <input
            className="clearInput"
            type="text"
            onChange={(e) => {
              console.log("changed input");
              setUpdateInputs(
                (map) => new Map(map.set(tableHeaders[i], e.target.value))
              );
            }}
          />
        </div>
      );
    }
    return inputBoxes;
  };

  useEffect(() => {
    getNames();
  }, []);

  return (
    <div>
      <h1>Welcome to Batman Table Visualizer</h1>
      <h3>Please choose a table to load</h3>
      <div className="tableLoaderDiv">
        <TableLoader tableNames={tableNames} change={setTableName} />
        <AwesomeButton
          type="primary"
          onPress={() => {
            getTable("1");
          }}
        >
          Load
        </AwesomeButton>
      </div>
      {tableRows.length > 0 ? <h2>Now Viewing: {tableName}</h2> : ""}
      <div className="majorRow">
        <div className="colLeft">
          <Table
            name={tableName}
            headers={tableHeaders}
            rows={tableRows}
            deleteFunc={deleteRow}
            sortFunc={getTable}
          />
        </div>
        <div className="colRight">
          <div>
            {tableRows.length > 0 ? (
              <React.Fragment>
                <div className="addDiv">
                  <h4>Add to Table</h4>
                  {loadAddInputBoxes()}
                  <button
                    className="modButton"
                    onClick={() => {
                      console.log("Adding new row");
                      console.log(addInputs);
                      // take inputs from boxes,
                      let toAdd: AddParams = {
                        name: tableName,
                        columns: Array.from(addInputs.keys()),
                        values: Array.from(addInputs.values()),
                      };
                      console.log(toAdd.columns);
                      console.log(toAdd.values);
                      addRow(toAdd);
                    }}
                  >
                    Add
                  </button>
                </div>

                <div className="updateDiv">
                  <h4>Update Table</h4>
                  {loadUpdateInputBoxes()}
                  <button
                    className="modButton"
                    onClick={() => {
                      console.log("Adding new row");
                      console.log(updateInputs);
                      // take inputs from boxes,
                      let toUpdate: UpdateParams = {
                        name: tableName,
                        row: tableRows[parseInt(rowToUpdate) - 1],
                        columns: Array.from(updateInputs.keys()),
                        values: Array.from(updateInputs.values()),
                      };
                      console.log(toUpdate.columns);
                      console.log(toUpdate.values);
                      updateRow(toUpdate);
                    }}
                  >
                    Update
                  </button>
                </div>
              </React.Fragment>
            ) : null}
          </div>
        </div>
      </div>
    </div>
  );
}
export default TableVisualizer;
