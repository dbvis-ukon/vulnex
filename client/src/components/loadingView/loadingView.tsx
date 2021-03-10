import * as React from 'react';
import './loadingView.css';
import Spinner from 'react-bootstrap/Spinner';

class LoadingView extends React.Component<any, any>  {
  public render() {
    return (
        <Spinner animation='border' variant='primary' />
    );
  }
}

export default LoadingView;
