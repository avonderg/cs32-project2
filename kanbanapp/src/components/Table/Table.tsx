import React, {useState} from 'react';
// import TableRow from 'TableRow'
import TableRow from '../TableRow/TableRow'

type table = {

    headerList: string[]
    rowMap: Record<string,string>[];
}

type kanban_info = {
    kanban_status: string[]
    kanban_text: string[][]
}

type tableprop = {
    tableInfo: table;
    setRowMap: (value: table) => void;
}


function Table(props: tableprop) {


    const rowMap = props.tableInfo.rowMap
    const kb_status:string[] = [];
    const kb_text:string[][] = [];


    var output_table: kanban_info = {
        kanban_status: kb_status,
        kanban_text: kb_text
    };

    for(let i = 0; i < rowMap.length; i++) {

        const relevant_columns:string[] = []
        relevant_columns[0] = "block_id"
        relevant_columns[1] = "block_title"
        relevant_columns[2] = "block_content"
        relevant_columns[3] = "block_date"
        const curr_row = rowMap[i]
        const curr_priority = rowMap[i]["name"]
        output_table.kanban_status[i] = curr_priority
        kb_text.push([])

        // Save relevant columns from the database into the kanban datatype.
        for(let j = 0; j < props.tableInfo.headerList.length; j++){
            var header_name: string = props.tableInfo.headerList[j]
            if(relevant_columns.includes(header_name)){
                kb_text[i].push(curr_row[props.tableInfo.headerList[j]])
            }

        }
    }
    return(
        <table className = "kanbanTable" id = "kanban_display">
            <tbody>
            <tr>
                <th>Need to Start</th>
                <th>Unassigned</th>
                <th>In Progress</th>
                <th>Completed</th>
            </tr>
            <TableRow kanban_status={output_table.kanban_status} kanban_text={output_table.kanban_text}/>
            </tbody>
        </table>
    )

}

function displayTable(rowMap: Record<string,string>[]) {
    const table = document.getElementById("kanban_display")! as HTMLTableElement
    const newRow = table.insertRow();
    const currCell = newRow.insertCell(1)
    const cellContents = document.createTextNode("bla")
    currCell.appendChild(cellContents)
}


export default Table;