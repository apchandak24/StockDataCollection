#write out current crontab
crontab -l > mycron
#echo new cron into cron file
echo "*/5 * * * * cd /home/ankita/workspace/GetStockPrice && sh getStockPrice.sh" >> mycron
#install new cron file
crontab mycron
rm mycron
