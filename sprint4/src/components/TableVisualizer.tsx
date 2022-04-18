import React, { useEffect } from "react";
import { useState } from "react";
import axios from "axios";
import Table from "./Table";
import TableLoader from "./TableLoader";
import DatabaseLoader from "./DatabaseLoader";
import { waitFor } from "@testing-library/dom";

/**
 * Visualizes and contains the modification functionality of the table
 * @returns the HTML to modify and visualize a table
 */
function TableVisualizer() {
  const [dbNames, setDBNames] = useState([]);
  const [dbName, setDBName] = useState("");

  const [tableRetrieved, setTableRetrieved] = useState(false);

  const [currTable, setCurrTable] = useState("");
  const [tableName, setTableName] = useState("");
  const [tableRows, setTableRows] = useState([]);
  const [tableNames, setTableNames] = useState([]);
  const [tableHeaders, setTableHeaders] = useState([]);

  const [addInputs, setAddInputs] = useState(new Map());
  const [updateInputs, setUpdateInputs] = useState(new Map());
  const [rowToUpdate, setRowToUpdate] = useState("");

  /**
   * The types used in making post requests
   */
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

  /**
   * the config for get and post requests.
   */
  let config = {
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
  };

  const getDbNames = () => {
    axios
      .get("http://localhost:4567/dbNames", config)
      .then((response) => {
        console.log(response.data);
        setDBNames(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  /**
   * This makes a get request to get the names of the tables in the database.
   */
  const getNames = () => {
    axios
      .get("http://localhost:4567/tableNames", config)
      .then((response) => {
        setTableNames(response.data);
        setTableName(response.data[0]);
      })
      .then((response) => console.log("get names: ", tableNames))
      .catch((error) => {
        console.log(error);
      });
  };

  const loadDB = () => {
    if (dbName == "") {
      return;
    }
    const toSend = {
      name: dbName,
    };
    axios
      .post("http://localhost:4567/loadDB", toSend, config)
      .then((response) => {
        console.log(response.data);
        getNames();
        setTableName(tableNames[0]);
        // console.log("loaded the following tables: " + tableNames);
        // setTableName(tableNames[0]);
        // console.log("Now the table name is: " + tableName);
      })
      .catch((error) => {
        console.log(error);
      });
    setTableName(tableNames[0]);
  };

  /**
   * This makes a post request to get all the rows of a table in the database.
   * @param sortCol, the column on which the table should be sorted by.
   */
  const getTable = (sortCol: string) => {
    if (tableName == "") {
      console.log("table name was null");
      return;
    }
    const toSend = {
      name: tableName,
      sortCol: sortCol,
    };
    axios
      .post("http://localhost:4567/table", toSend, config)
      .then((response) => {
        console.log("getting table: " + tableName);
        console.log("the database is: " + dbName);
        console.log(response.data);
        setTableRows(response.data["rows"]);
        setTableHeaders(response.data["headers"]);
        setCurrTable(tableName);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  /**
   * makes a post request to add a row to the database.
   * @param toSend of AddParams, the information we are adding to the database.
   */
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

  /**
   * makes a post request to delete a row from the table.
   * @param rowToDelete a Record representing the row we are deleting
   */
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

  /**
   * makes a post request to update a given row in the table.
   * @param toSend of type UpdateParams, the information we are passing to the
   * api to make the post request.
   */
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

  /**
   * Creates a list of input boxes for each column of the table.
   * @returns a list of inputs, one for each column of the table.
   */
  const loadAddInputBoxes = () => {
    let inputBoxes = [];
    for (let i = 0; i < tableHeaders.length; i++) {
      inputBoxes.push(
        <div className="inputField">
          <label htmlFor={tableHeaders[i]}>{tableHeaders[i]}:</label>
          <input
            className="clearInput inputBox"
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

  /**
   * Creates a list of input boxes to serve the update feature.
   * @returns a list of inputs, one for the row number, and one for each column
   * of the table.
   */
  const loadUpdateInputBoxes = () => {
    let inputBoxes = [];
    inputBoxes.push(
      <div className="inputField">
        <label htmlFor="rowNum">Row Number: </label>
        <input
          className="clearInputNum updateBox"
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
            className="clearInput updateBox"
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

  /**
   * calls getNames when the page is loaded.
   */
  useEffect(() => {
    getDbNames();
  }, []);

  return (
    <div>
      <h1>Welcome to Batman Table Visualizer</h1>
      <h3>Please choose a database and table to load</h3>
      <div className="tableLoaderDiv">
        <DatabaseLoader dbNames={dbNames} change={setDBName} />
        <button
          className="awesomeButton dbLoadButton"
          onClick={() => {
            loadDB();
            console.log("these are the current tables: " + tableNames);
          }}
        >
          Load
        </button>
        <TableLoader tableNames={tableNames} change={setTableName} />
        <button
          className="awesomeButton tableLoadButton"
          onClick={() => {
            console.log(tableName);
            getTable("1");
          }}
        >
          Load
        </button>
      </div>
      {/* only load any table elements if a table has been loaded */}
      {tableRows.length > 0 ? <h2>Now Viewing: {currTable}</h2> : ""}
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
                {/* the HTML element containing the add form */}
                <div className="addDiv">
                  <h4>Add to Table</h4>
                  {loadAddInputBoxes()}
                  <button
                    className="modButton insertButton"
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
                {/* the HTML element containing the update form */}
                <div className="updateDiv">
                  <h4>Update Table</h4>
                  {loadUpdateInputBoxes()}
                  <button
                    className="modButton updateButton"
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
