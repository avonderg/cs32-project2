import React from "react";
// import "./App.css";
import TableRow from "./TableRow";
import TableCell from "./TableCell";
import { useState } from "react";
import axios from "axios";
// @ts-ignore
import { AwesomeButton } from "react-awesome-button";
// @ts-ignore
import "react-awesome-button/dist/styles.css";

// interface representing table input
interface TableInput {
  name: string;
  headers: string[];
  rows: Record<string, string>[];
  deleteFunc: (r: Record<string, string>) => void;
  sortFunc: (col: string) => void;
}

/**
 * loads a table from a list of rows and other attributes
 * @param props a TableInput containing the information needed to load a table
 * @returns a loaded table
 */
function Table(props: TableInput) {
  const loadHeaders = () => {
    let tableHeaders = [];
    if (props.rows.length > 0) {
      tableHeaders.push(<th>Row #</th>);
    }
    // creating the headers for the table
    for (let i = 0; i < props.headers.length; i++) {
      tableHeaders.push(
        <th>
          {props.headers[i]}
          <button
            className="sortButton"
            onClick={() => {
              props.sortFunc(props.headers[i]);
            }}
          >
            sort
          </button>
        </th>
      );
    }
    tableHeaders.push(<th></th>);
    return tableHeaders;
  };

  const loadRows = () => {
    // need to create the header map
    let headerMap = new Map<number, string>();
    for (let i = 0; i < props.headers.length; i++) {
      headerMap.set(i, props.headers[i]);
    }
    // for each row, we create a row, passing in the record and the headermap
    let rows = [];
    for (let i = 0; i < props.rows.length; i++) {
      let currRecord: Record<string, string> = props.rows[i];
      rows.push(
        <TableRow
          row={currRecord}
          rowNum={(i + 1).toString()}
          headerMap={headerMap}
          deleteFunc={props.deleteFunc}
        />
      );
    }
    return rows;
  };

  return (
    <div>
      {props.rows.length > 0 ? (
        <table>
          <tr>{loadHeaders()}</tr>
          <tbody>{loadRows()}</tbody>
        </table>
      ) : (
        ""
      )}
    </div>
  );
}

export default Table;
