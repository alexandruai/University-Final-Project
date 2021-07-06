#!/usr/bin/bash

tk=$(echo '{"username":"lucaci32u4", "password":"aaa123"}' | http POST https://andra.lucaci32u4.xyz/api/login -b)

prize=$(http GET https://andra.lucaci32u4.xyz/api/list TokenAuth:$tk)

echo $prize

priza=$(echo $prize | jq '.[0]')

state=$(http GET https://andra.lucaci32u4.xyz/api/socket/$priza TokenAuth:$tk)
echo $state

state = "[true, false, true, true]"

state=$(echo $state | http POST https://andra.lucaci32u4.xyz/api/socket/$priza TokenAuth:$tk)