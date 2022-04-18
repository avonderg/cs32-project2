import React from "react";
import TableCell from "./TableCell";

// interface representing the input to the table row 
interface TableRowInput {
  row: Record<string, string>;
  rowNum: string;
  headerMap: Map<number, string>;
  deleteFunc: (r: Record<string, string>) => void;
}

/**
 * returns a TableRow component
 * @param props type TableRowInput, contains the info that goes into a table row
 * @returns an HTML element representign a table row
 */
function TableRow(props: TableRowInput) {
  const loadRow = () => {
    let cells = [];
    cells.push(<TableCell text={props.rowNum} />);
    // adds a table cell for every column of the table
    for (let i = 0; i < props.headerMap.size; i++) {
      let col: string = String(props.headerMap.get(i));
      let value = props.row[col];
      cells.push(<TableCell text={value} />);
    }
    // adding a delete button for every row
    cells.push(
      <td>
        <button
          className="deleteButton"
          onClick={() => {
            console.log("deleting row");
            console.log(props.row);
            props.deleteFunc(props.row);
          }}
        >
          Delete
        </button>
      </td>
    );
    return cells;
  };
  return <tr id={props.rowNum}>{loadRow()}</tr>;
}

export default TableRow;
