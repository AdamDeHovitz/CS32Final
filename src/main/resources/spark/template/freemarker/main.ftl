<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/ionic.min.css">    
  </head>
  <body ng-app="bulkApp">
	<ion-nav-bar class="bar-stable">
	  <ion-nav-back-button>
      </ion-nav-back-button>
    </ion-nav-bar>

	<ion-nav-view></ion-nav-view>

     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
	 <script src="js/ionic.bundle.min.js"></script> 
	 <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.4.0/angular-messages.js"></script>
	 <script src="js/app.js"></script>
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
     <script src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
