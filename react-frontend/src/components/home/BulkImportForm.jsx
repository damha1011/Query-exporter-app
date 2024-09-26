import React from 'react';
import { InboxOutlined } from '@ant-design/icons';
import { message, notification, Upload } from 'antd';
import { openNotification } from '../../utils';

const BulkImportForm = ({ state, setState }) => {
  const { Dragger } = Upload;
  const token = localStorage.getItem('token');
  const apiBaseUrl = process.env.REACT_APP_API_BASE_URL;
  const [api, contextHolder] = notification.useNotification();

  const props = {
    name: 'file',
    action: apiBaseUrl + '/upload-yaml',
    headers: {
        'Authorization': `Bearer ${token}`,
        // 'Content-Type': 'multipart/form-data',
    },
    onChange(info) {
      const { status } = info.file;
      if (status === 'done') {
        message.success(`${info.file.name} file uploaded successfully.`);
        setState({...state, refresh: !state.refresh});
      } else if (status === 'error') {
        message.error(`${info.file.name} file upload failed.`);
        openNotification(api, 'error', 'File upload failed', info.file?.response?.message);
      }
    },
    onDrop(e) {
      console.log('Dropped files', e.dataTransfer.files);
    },
  };

  return (
    <div>
      {contextHolder}
      <Dragger{...props}>
        <p className="ant-upload-drag-icon">
          <InboxOutlined />
        </p>
        <p className="ant-upload-text">Click or drag file to this area to upload</p>
        <p className="ant-upload-hint">
          Support for a single or bulk upload. Strictly prohibited from uploading company data or other
          banned files.
        </p>
      </Dragger>
    </div>
  );
};

export default BulkImportForm;
