<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/ionic.min.css">    
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/chat.css">
    <link rel="stylesheet" href="css/toaster.css">
  </head>
  <body ng-app="bulkApp">
	<ion-nav-bar class="bar-stable">
	  <ion-nav-back-button>
      </ion-nav-back-button>
    </ion-nav-bar>
	<toaster-container toaster-options="{'time-out': 5000, 'close-button':false, 'animation-class': 'toast-top-center'}"></toaster-container>
	
	<ion-nav-view></ion-nav-view>

     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAV2Ed7TgQofJ_W0NpskOYg6XplP_0aN_s"></script>
     <script src="js/jquery-2.1.1.js"></script>
	 <script src="js/ionic.bundle.min.js"></script> 
	 <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.4.0/angular-messages.js"></script>
	 <script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.8.4/moment.min.js"></script>
     <script src="//cdnjs.cloudflare.com/ajax/libs/angular-moment/0.10.3/angular-moment.min.js"></script>
	 <script src="js/app.js"></script>
	 <script src="js/toaster.js"></script>
	 <script src="js/msdElastic.js"></script>
	 <script src="js/autolinker.js"></script>
	 <script src="js/loginCtrl.js"></script>
	 <script src="js/feedCtrl.js"></script>
	 <script src="js/accountCtrl.js"></script>
	 <script src="js/eventsCtrl.js"></script>
	 <script src="js/myEventsCtrl.js"></script>
	 <script src="js/pendingEventsCtrl.js"></script>
	 <script src="js/joinedEventsCtrl.js"></script>
	 <script src="js/eventCtrl.js"></script>
	 <script src="js/profileCtrl.js"></script>
	 <script src="js/reviewsCtrl.js"></script>
	 <script src="js/chatCtrl.js"></script>
	 <script src="js/requestsCtrl.js"></script>
	 <script src="js/mapCtrl.js"></script>
	 <script src="js/membersCtrl.js"></script>
	 <script src="js/settingsCtrl.js"></script>
	 <script src="js/writeReviewCtrl.js"></script>
	 <script src="js/aboutCtrl.js"></script>
	 
	 
	 
     <script src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
