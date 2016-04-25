bulkAppControllers.controller("eventCtrl", 
	function($scope, $http, $rootScope, $stateParams, $ionicPopup, $state) {

	var eventId = $stateParams.eventId;
	console.log(eventId);
	
	 // A confirm dialog
	 $scope.showConfirm = function() {
	   var confirmPopup = $ionicPopup.confirm({
	     title: 'Confirm Join',
	     template: 'Are you sure you want to join this event?'
	   });

	   confirmPopup.then(function(res) {
	     if(res) {
	       console.log('You are sure');
	     } else {
	       console.log('You are not sure');
	     }
	   });
	 };
	 
	$.post("/event-view", {id: eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.event = responseObject;
			var isOwner = responseObject.authorId == $rootScope.account.id;
			var isMember = false;
			for (var i = 0; i < responseObject.members.length; i++) {
				if (responseObject.members[i] == $rootScope.account.id) {
					isMember = true;
				}
			}
			$scope.isOwner = isOwner;
			$scope.isMember = isMember;
			console.log(responseObject);
	});

	
	 $scope.goAuthor = function() {
		 $state.go('tab.profile',{userId: $scope.event.authorId});
	 }

		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/
});