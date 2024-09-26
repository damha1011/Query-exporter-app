import React, { useEffect, useState } from 'react'
import HomeService from '../../services/HomeService'
import BulkImportForm from './BulkImportForm'
import { DownloadOutlined } from '@ant-design/icons';
import { Button, Space, Typography } from 'antd';
import { headers } from '../../utils';
import ListComponent from './listComponent';

const HomeComponent = () => {
  const [state, setState] = useState({
    data: [],
    refresh: false
  });

  const apiBaseUrl = process.env.REACT_APP_API_BASE_URL;
  const token = localStorage.getItem('token');

  useEffect(() => {
    HomeService.homeView(localStorage.getItem('token')).then((res) => {
      setState({
        ...state,
        data: res.data
      });
    });
  }, [state.refresh])

  const downloadYaml = (e) =>  {
    e.preventDefault();
    fetch(apiBaseUrl+'/home/downloadYaml', {
      headers: headers(token),
    }) // Replace with your API endpoint
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.blob();
      })
      .then(blob => {
        const url = window.URL.createObjectURL(new Blob([blob]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'file.yaml'); // Set the filename
        document.body.appendChild(link);
        link.click();
        link.parentNode.removeChild(link);
      })
      .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
      });
  }

  return (
    <div>
      <ListComponent state={state} setState={setState}/>
      <div>
        <Space size="small">
          <Button 
            type="primary" 
            icon={<DownloadOutlined />} 
            size="large" 
            className='mb-2'
            onClick={downloadYaml}
          >
            Download
          </Button>
          <Typography.Text italic type="secondary">Click to download the configuration file.</Typography.Text>
        </Space>
        <BulkImportForm state={state} setState={setState}/>
      </div>
    </div>
  )
}

export default HomeComponent;
