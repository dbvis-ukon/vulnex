import React from 'react';
import './App.css';
import Sidebar from 'components/sidebar';
import Navbar from 'components/navbar';
import SwitchableView from 'components/switchableView';
import Tooltip from 'components/tooltip';
import SearchPopup from 'components/treeView/tableRowHeader/searchPopup';
import { REPOSITORY_TABLE } from 'reducers/viewChoiceReducer';

function App() {
    return (
        <div className='App'>
            <Navbar viewChoice={REPOSITORY_TABLE}></Navbar>
            <div className='d-flex'>
                <Sidebar numDependencies={0} numErrors={0} numWarnings={0} minCvss={0.0} maxCvss={10.0} showAllModules={false} showAllBugs={false} bugDisplay={false} />
                <SwitchableView viewChoice={REPOSITORY_TABLE}></SwitchableView>
            </div>
            <Tooltip visible={false} xPos={0} yPos={0} content='' />
            <SearchPopup visible={false} topBugs={[]} />
        </div>
    );
}

export default App;
