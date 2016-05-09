

bulkAppControllers.controller("loginCtrl",
	function($scope, $rootScope, $state, $rootScope, $ionicViewSwitcher, 
		$ionicModal, $http, $ionicPopup, $interval, toaster) {

	$scope.loginData = {};
	$scope.registerData = {};
	$scope.forms = {};
	//$scope.fd = = new FormData();
	
	$scope.$on('$ionicView.enter', function() {
	  	$scope.forms.registerForm.$setPristine();
	  	$scope.forms.registerForm.$setUntouched();
	});



	$ionicModal.fromTemplateUrl('register-modal.html', {
	    scope: $scope,
	    animation: 'slide-in-up'
	  }).then(function(modal) {
	    $scope.modal = modal;
	  });
	  $scope.openModal = function() {
	    $scope.modal.show();
	  };
	  $scope.closeModal = function() {
	    $scope.modal.hide();
	    $scope.registerData = {};
	  	$scope.forms.registerForm.$setPristine();
	  	$scope.forms.registerForm.$setUntouched();

	  };
	  //Cleanup the modal when we're done with it!
	  $scope.$on('$destroy', function() {
	    $scope.modal.remove();
	  });
	  // Execute action on hide modal
	  $scope.$on('modal.hidden', function() {
	    // Execute action
	  });
	  // Execute action on remove modal
	  $scope.$on('modal.removed', function() {
	    // Execute action
	});
	  
	$rootScope.stopNotifications = undefined;
	
	var getNotifications = function() {
		// post request for notifications

		$.post("/notification", {"id": $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);

		});
		
		/*
		var rand = Math.floor((Math.random() * 100) + 1);
		if (rand > 100) {
			console.log("new msg");
			var f = function() {console.log("route!!");toaster.clear()};
			toaster.pop({type: 'note', title: "title", body:"text", 
				timeout: 50000, clickHandler: f, showCloseButton: false});			// new msg
		}*/
		
	};

	//TODO: resize when something touched to make sure can scroll (scrolldel.resize)
	/*
    $scope.storeFile = function(files) {

        $scope.fd.append("file", files[0]);
        console.log($scope.fd);
        }*/
	$scope.doRegister = function() {
		console.log('Doing register', $scope.registerData);

		var curData = $scope.registerData;
		var regisData = {
                firstName: curData.firstName,
                lastName: curData.lastName,
                email: curData.email,
                password: curData.password,
                image: curData.img
        };
        console.log(regisData);

        /*
        $http.post(uploadUrl, fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            }).success( ...all right!... ).error( ..damn!... );

        };*/

/*
        console.log($scope.fd;
        $scope.fd.append("firstName" : curData.firstName);
        $scope.fd.append("lastName" : curData.lastName);
        $scope.fd.append("email" : curData.email);
        $scope.fd.append("password" : curData.password);

        {
                		  "firstName" : curData.firstName,
                		  "lastName" : curData.lastName,
                		  "email" : curData.email,
                		  "password" : curData.password,
                		  "image" : $scope.file}
                		*/
        $.post("/register", {
        		  "firstName" : curData.firstName,
        		  "lastName" : curData.lastName,
        		  "email" : curData.email,
        		  "password" : curData.password,
        		  "image" : curData.img
        		},
           function(responseJSON) {
        	responseObject = JSON.parse(responseJSON);
        	console.log(responseObject);
          $scope.closeModal();
          if (responseObject.hasError) {
          	$scope.showAlert('Registration Failed', responseObject.errorMsg);
          }
          else {
          	$rootScope.account = {name: responseObject.name,
      	    		id: responseObject.id,
      	    		joined: responseObject.date,
      	    		rating: 5,
      	    		img: responseObject.picture,
      	    		reviews: responseObject.reviews};
      	    $rootScope.authenticated = true;
      	    $rootScope.stopNotifications = $interval(getNotifications, 5000);
      	    $ionicViewSwitcher.nextDirection('forward');
      	 		$state.go("tab.feed");
          }
	 	});
	};

	$scope.showAlert = function(curTitle, curMsg) {
	   var alertPopup = $ionicPopup.alert({
	     title: curTitle,
	     template: curMsg
	   });

	   alertPopup.then(function(res) {
	   });
	 };

	$scope.doLogin = function() {
		console.log('Doing login', $scope.loginData);

		$.post("/login", {
                "username": $scope.loginData.email,
                "password": $scope.loginData.password,
        }, function(responseJSON) {
        	console.log(responseJSON);
        	responseObject = JSON.parse(responseJSON);
        	if (responseObject.hasError) {
        		$scope.showAlert('Authentication Failed', 'Username or password is incorrect.');
        	} else {
	        	$rootScope.account = {name: responseObject.name,
	    		id: responseObject.id,
	    		joined: responseObject.date,
	    		rating: 5,
	    		img: responseObject.picture,
	    		reviews: responseObject.reviews};
	    		$rootScope.authenticated = true;
	    		$rootScope.stopNotifications = $interval(getNotifications, 5000);
	    		$ionicViewSwitcher.nextDirection('forward');
	    		$state.go("tab.feed");
	 		};
	 		$scope.loginData = {};
		  	$scope.forms.loginForm.$setPristine();
		  	$scope.forms.loginForm.$setUntouched();
	 	});
  	};
});
/*
bulkAppcontrollers.controller("loginCtrl", ['$scope', '$http', '$rootScope', '$location',
	function($scope, $http) {
		$scope.longin = function(username, password) {
			var postData = $.param({
				json: JSON.stringify({
					username: username,
					password: password
				})
			});
			$http.post("/login", postData).success(function(data, status) {
				$rootScope.userId = data.userID;
				$location.path("/home");
			});
		}

	}]);*/